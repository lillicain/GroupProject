package com.example.groupproject.camera

import ARG_MEDIA_PATH
import ARG_PREVIEW_TYPE
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Camera
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.groupproject.R
import com.example.groupproject.databinding.FragmentVideoBinding
import com.example.groupproject.utils.CameraConstant
import com.example.groupproject.utils.FileManager
import com.example.groupproject.utils.MediaType
import com.example.groupproject.utils.OutputFileOptionsFactory
import com.example.groupproject.utils.getDimensionRatioString
import com.example.groupproject.utils.getNameString
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Consumer
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

    class VideoFragment : Fragment() {

    private val viewModel: CameraViewModel by viewModels()
    private lateinit var binding: FragmentVideoBinding

    private lateinit var videoCapture: VideoCapture<Recorder>
    private var currentRecording: Recording? = null
    private lateinit var recordingState: VideoRecordEvent

    private var camera: Camera? = null

    private lateinit var cameraExecutor: ExecutorService
    private val mainThreadExecutor by lazy { ContextCompat.getMainExecutor(requireContext()) }

    private lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewFinder.setOnTouchListener { _, motionEvent ->
            scaleGestureDetector.onTouchEvent(motionEvent)
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
//                    binding.zoomSeekWrapper.visibility = View.VISIBLE
                    startTouchTimer()
                    return@setOnTouchListener true
                }

                MotionEvent.ACTION_UP -> {
                    val factory = binding.viewFinder.meteringPointFactory
                    val point = factory.createPoint(motionEvent.x, motionEvent.y)
                    val action = FocusMeteringAction.Builder(point).build()

//                    camera?.cameraControl?.startFocusAndMetering(action)

                    return@setOnTouchListener true
                }

                else -> return@setOnTouchListener false

            }
        }

        binding.recordButton.setOnClickListener {
            if (!this@VideoFragment::recordingState.isInitialized || recordingState is VideoRecordEvent.Finalize) {
                enabledRecording(true)
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
//                startRecording()
            }

        }

        binding.ivPause.setOnClickListener {
            when (recordingState) {
                is VideoRecordEvent.Start -> {
                    currentRecording?.pause()
                }

                is VideoRecordEvent.Pause -> {
                    currentRecording?.resume()
                }

                is VideoRecordEvent.Resume -> {
                    currentRecording?.pause()
                }

                else -> throw IllegalStateException("recordingState in unknown state")
            }
        }

        binding.ivStop.setOnClickListener {
            if (currentRecording == null || recordingState is VideoRecordEvent.Finalize) {
                return@setOnClickListener
            }

            stopRecording()

        }


        binding.swCameraOption.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                findNavController().navigate(R.id.actionToCameraFragment)
//                findNavController().navigate(R.id.action_videoFragment_to_cameraFragment)
            }
        }

        binding.pnlFacing.setOnClickListener(facingChangeListener)
        binding.pnlSound.setOnClickListener(soundChangeListener)
//        binding.zoomSeekBar.setOnSeekBarChangeListener(zoomSeekListener)
        scaleGestureDetector = ScaleGestureDetector(requireContext(), zoomListener)

        cameraExecutor = Executors.newSingleThreadExecutor()

        bindCaptureUseCase()

        observeUriState()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    private fun observeUriState() {
        viewModel.audioEnabled.observe(requireActivity()) { state ->
            binding.ivVolume.setImageResource(if (state == true) R.drawable.ic_volume_on else R.drawable.ic_volume_off)
        }
    }


    private fun bindCaptureUseCase() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


            val metrics = DisplayMetrics().also { binding.viewFinder.display.getRealMetrics(it) }
            val aspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
            val rotation = binding.viewFinder.display.rotation


            binding.viewFinder.updateLayoutParams<ConstraintLayout.LayoutParams> {
                val orientation = resources.configuration.orientation
                dimensionRatio =
                    aspectRatio.getDimensionRatioString((orientation == Configuration.ORIENTATION_PORTRAIT))
            }

            val localCameraProvider =
                cameraProvider ?: throw IllegalStateException("Camera initialization failed.")


            val preview =
                Preview.Builder().setTargetAspectRatio(aspectRatio) // set the camera aspect ratio
                    .setTargetRotation(rotation) // set the camera rotation
                    .build().apply {
                        setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }

            val quality = QualitySelector.from(
                Quality.HIGHEST, FallbackStrategy.higherQualityOrLowerThan(
                    Quality.HD
                )
            )

            val recorder = Recorder.Builder().setQualitySelector(quality).build()
            videoCapture = VideoCapture.withOutput(recorder)



            try {
                localCameraProvider.unbindAll()
//                camera = localCameraProvider.bindToLifecycle(viewLifecycleOwner, viewModel.lensFacing, videoCapture, preview)
            } catch (exc: Exception) {
                Log.e("CameraFragment", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))


    }

//    @SuppressLint("MissingPermission")
//    private fun startRecording() {
//
//
//        currentRecording = videoCapture.output.prepareRecording(
//            requireActivity(),
//            OutputFileOptionsFactory().getVideoOutputFileOption(requireActivity())
//        ).apply { if (viewModel.audioEnabled.value!!) withAudioEnabled() }
//            .start(mainThreadExecutor, captureListener)
//
//        Log.i(TAG, "Recording started")
//    }

    private fun stopRecording() {
        if (currentRecording == null || recordingState is VideoRecordEvent.Finalize) {
            return
        }

        val recording = currentRecording
        if (recording != null) {
            recording.stop()
            currentRecording = null
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUI(event: VideoRecordEvent) {
        val state = if (event is VideoRecordEvent.Status) recordingState.getNameString()
        else event.getNameString()
        when (event) {
            is VideoRecordEvent.Status -> {}
            is VideoRecordEvent.Start -> {
                binding.pnlTimer.visibility = View.VISIBLE
                binding.pnlPreview.visibility = View.GONE
                binding.pnlSound.visibility = View.GONE
            }

            is VideoRecordEvent.Finalize -> {
                binding.pnlTimer.visibility = View.GONE
                binding.pnlSound.visibility = View.VISIBLE
                enabledRecording(false)
            }

            is VideoRecordEvent.Pause -> {
                binding.ivPause.setImageResource(R.drawable.ic_resume)
            }

            is VideoRecordEvent.Resume -> {
                binding.ivPause.setImageResource(R.drawable.ic_pause)
            }
        }

        val stats = event.recordingStats
        val size = stats.numBytesRecorded / 1000
        val time = java.util.concurrent.TimeUnit.NANOSECONDS.toSeconds(stats.recordedDurationNanos)
        var text = "${state}: recorded ${size}KB, in ${time}second"
        if (event is VideoRecordEvent.Finalize) text =
            "${text}\nFile saved to: ${event.outputResults.outputUri}"

        Log.i(TAG, "recording event: $text")

        val millisUntilFinished = (time * 1000)

        val minute = (millisUntilFinished / 60000)
        val second = (millisUntilFinished % 60000) / 1000
        val secondText = if (second > 9) second.toString() else "0$second"
        val minuteText = if (minute > 9) second.toString() else "0$minute"

        binding.tvTimerTick.text = "$minuteText:$secondText"
        println("Timer  : $minuteText:$secondText")


    }


    private fun enabledRecording(enable: Boolean) {

        binding.pnlStartRecording.isEnabled = !enable
        binding.pnlRecording.isEnabled = enable

        if (enable) {
            binding.pnlStartRecording.visibility = View.INVISIBLE
            binding.pnlRecording.visibility = View.VISIBLE
            binding.cameraOption.visibility = View.GONE
        } else {
            binding.pnlStartRecording.visibility = View.VISIBLE
            binding.pnlRecording.visibility = View.GONE
            binding.cameraOption.visibility = View.VISIBLE
            binding.ivPause.setImageResource(R.drawable.ic_pause)
        }

    }


    private val captureListener = Consumer<VideoRecordEvent> { event ->
        if (event !is VideoRecordEvent.Status) recordingState = event

        updateUI(event)

        if (event is VideoRecordEvent.Finalize) {
            lifecycleScope.launch {
                setPreview(event.outputResults.outputUri)
                viewModel.savedUri.value = event.outputResults.outputUri
            }
        }
    }

    private val facingChangeListener = View.OnClickListener {
        flipCamera()
    }

    private val soundChangeListener = View.OnClickListener {
        viewModel.toggleSound()
    }

    private val zoomListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
//            val scale = camera?.cameraInfo?.zoomState?.value?.zoomRatio?.times(detector.scaleFactor)
//            scale?.let { value ->
//                camera?.cameraControl?.setZoomRatio(value)
//                val result = if (value < 1.5) 0.0f else value
//                binding.zoomSeekBar.setOnSeekBarChangeListener(null)
//                binding.zoomSeekBar.progress = (result * 10).toInt()
//                binding.zoomSeekBar.setOnSeekBarChangeListener(zoomSeekListener)
//            }

//            binding.zoomSeekWrapper.visibility = View.VISIBLE
            startTouchTimer()

            return true
        }
    }

    private val zoomSeekListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
//            camera?.cameraControl?.setLinearZoom(progress / 100.toFloat())
            startTouchTimer()
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {

        }

    }

    private fun flipCamera() {
        var rotationDegree = 0f
        if (viewModel.lensFacing === CameraSelector.DEFAULT_FRONT_CAMERA) {
            viewModel.lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
            rotationDegree = 180f
        } else if (viewModel.lensFacing === CameraSelector.DEFAULT_BACK_CAMERA) {
            viewModel.lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA
            rotationDegree = -180f
        }

        bindCaptureUseCase()

        binding.ivFacing.animate().rotation(rotationDegree).setDuration(500).start()
    }


    private fun setPreview(uri: Uri) {
        binding.pnlPreview.visibility = View.VISIBLE


        val fileSize = FileManager.getFileSizeFromUri(requireContext(), uri)
        if (fileSize == null || fileSize <= 0) {
            Log.e("VideoViewerFragment", "Failed to get recorded file size, could not be played!")
            return
        }

        val filePath = FileManager.getAbsolutePathFromUri(requireContext(), uri) ?: return
        val fileInfo = "FileSize: $fileSize\n $filePath"
        Log.i("VideoViewerFragment", fileInfo)


        val mSize = Size(96, 96)
        val ca = CancellationSignal()
        val bitmapThumbnail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requireActivity().contentResolver.loadThumbnail(uri, mSize, ca)
        } else {
            ThumbnailUtils.createVideoThumbnail(
                filePath, MediaStore.Video.Thumbnails.MICRO_KIND
            )
        }

        binding.previewImage.setImageBitmap(bitmapThumbnail)


        binding.pnlPreview.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ARG_PREVIEW_TYPE, MediaType.VIDEO)
            bundle.putString(ARG_MEDIA_PATH, uri.toString())
            findNavController().navigate(R.id.preview, bundle)
        }

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - CameraConstant.RATIO_4_3_VALUE) <= abs(previewRatio - CameraConstant.RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private var revealTimer: CountDownTimer? = null
    private fun startTouchTimer(duration: Long = 2000) {
        revealTimer?.cancel()
        revealTimer = null
        revealTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
//                binding.zoomSeekWrapper.visibility = View.INVISIBLE
            }
        }.start()
    }

    companion object {
        val TAG: String = VideoFragment::class.java.simpleName
    }
}