package com.example.groupproject.camera

import ARG_MEDIA_PATH
import ARG_PREVIEW_TYPE
import android.Manifest.*
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity.ScreenCaptureCallback
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.PixelCopy
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.camera.camera2.internal.compat.workaround.TargetAspectRatio.RATIO_16_9
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.updateLayoutParams
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.groupproject.R
import com.example.groupproject.R.*
import com.example.groupproject.R.id.*
import com.example.groupproject.camera.CameraViewModel.Companion.TAG
import com.example.groupproject.camera.VideoFragment.Companion.TAG
import com.example.groupproject.databinding.FragmentCameraBinding
import com.example.groupproject.preference.CameraXLivePreviewPreferenceFragment
import com.example.groupproject.preference.GraphicOverlay
import com.example.groupproject.preference.PreferenceUtils
import com.example.groupproject.utils.MediaType
import com.example.groupproject.utils.OutputFileOptionsFactory
import com.example.groupproject.utils.getAspectRationString
import com.example.groupproject.utils.getDimensionRatioString
import com.google.android.material.shape.CornerSize
import com.google.androidbrowserhelper.trusted.Utils
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.max
import kotlin.math.min

data class BoxWithText(val box: Rect, val text: String)

class CameraFragment : Fragment() {
    lateinit var cameraExecutor: ExecutorService
    lateinit var scaleGestureDetector: ScaleGestureDetector
    lateinit var binding: FragmentCameraBinding
    var imageCapture: ImageCapture? = null
    var camera: Camera? = null
    val viewModel: CameraViewModel by viewModels()
    lateinit var result: String
lateinit var outputDirectory: File

    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private lateinit var captureImageFab: Button
    private lateinit var inputImageView: ImageView
    private lateinit var imgSampleOne: ImageView
    private lateinit var imgSampleTwo: ImageView
    private lateinit var imgSampleThree: ImageView
    private lateinit var tvPlaceholder: TextView
    private lateinit var currentPhotoPath: String
    lateinit var graphicOverlay: GraphicOverlay
    private var cameraProvider: ProcessCameraProvider? = null
   lateinit var bitmap: Bitmap
    private val filterListener = View.OnClickListener {
        changeFilter()
    }
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    lateinit var safeContext: Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
        graphicOverlay.overlay
    }

    private fun getStatusBarHeight(): Int {
        val resourceId =
                safeContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            safeContext.resources.getDimensionPixelSize(resourceId)
        } else 0
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
//        btn = (Button) view.findViewById(R.id.cameraButton)
//        String text = getA


        return binding.root

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
cameraExecutor = Executors.newSingleThreadExecutor()


        binding.viewFinder.setOnTouchListener { _, motionEvent ->
            scaleGestureDetector.onTouchEvent(motionEvent)
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.pnlFlashOptions.visibility = View.GONE
                    binding.pnlRatioOptions.visibility = View.GONE
                    startTouchTimer()
                    return@setOnTouchListener true
                }

                MotionEvent.ACTION_UP -> {
                    val factory = binding.viewFinder.meteringPointFactory
                    val point = factory.createPoint(motionEvent.x, motionEvent.y)
                    val action = FocusMeteringAction.Builder(point).build()
                    camera?.cameraControl?.startFocusAndMetering(action)
                    return@setOnTouchListener true
                }

                else -> return@setOnTouchListener false
            }
        }

//        captureImageFab = findViewById(R.id.captureImageFab)
//        inputImageView = findViewById(R.id.imageView)
//        imgSampleOne = findViewById(R.id.imgSampleOne)
//        imgSampleTwo = findViewById(R.id.imgSampleTwo)
//        imgSampleThree = findViewById(R.id.imgSampleThree)
//        tvPlaceholder = findViewById(R.id.tvPlaceholder)
//
//        captureImageFab.setOnClickListener(this)
//        imgSampleOne.setOnClickListener(this)
//        imgSampleTwo.setOnClickListener(this)
//        imgSampleThree.setOnClickListener(this)

        updateRatioView()
//        binding.scrollView.setOnClickListener {
//
//        }
//        binding.filterOne.setOnClickListener {
//
//        }
//        binding.filterTwo.setOnClickListener {
//            binding.filterTwo.text = "Filter2"
//        }
//        binding.filterThree.setOnClickListener {
//            binding.filterThree.setText("Filter3")
//        }

//        binding.viewFinder.setOnClickListener { runObjectDetection(bitmap) }
//        binding.filterThree.setOnClickListener { changeFilter() }
//        binding.graphicOverlayFinder.setOnClickListener { result }
//        binding.overlay.setOnClickListener { overlay.getAspectRationString(true)  }


        binding.viewFinder.setOnClickListener { startCamera() }
        binding.photoButton.setOnClickListener { takePhoto() }
        binding.pnlFLash.setOnClickListener(flashClickListener)
        binding.overlay.setOnClickListener { runObjectDetection(bitmap) }
//        binding.ivFlashOff.setOnClickListener(flashChangeListener)
//        binding.ivFlashOn.setOnClickListener(flashChangeListener)
//        binding.ivFlashAuto.setOnClickListener(flashChangeListener)
        binding.pnlFacing.setOnClickListener(facingChangeListener)
        binding.pnlRatio.setOnClickListener(ratioClickListener)
        binding.tvRatio169.setOnClickListener(ratioChangeListener)
        binding.tvRatio43.setOnClickListener(ratioChangeListener)
        scaleGestureDetector = ScaleGestureDetector(requireContext(), zoomListener)
//        binding.swCameraOption.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                findNavController().navigate(R.id.previewFragment)
//            }
//        }
        cameraExecutor = Executors.newSingleThreadExecutor()
        bindCameraUseCases()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateRatioView()
    }

    fun changeFilter() {
//        binding.filterOne.text = "Filter one"
        binding.filter.text = result
//        binding.cameraResult
        if (result.isEmpty()) {
            val result = "Evil"
        }

    }

    fun takePhoto() {
        binding.captureProgress.visibility = View.VISIBLE
        val imageCapture = imageCapture ?: return
        imageCapture.takePicture(
                OutputFileOptionsFactory().getPhotoOutputFileOption(requireActivity()),
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
//                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        viewModel.savedUri.value
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        output.savedUri?.let { setPreview(it) }
                        viewModel.savedUri.value = output.savedUri
                        binding.captureProgress.visibility = View.VISIBLE
                    }
                })

    }

    fun bindCameraUseCases() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        binding.viewFinder.updateLayoutParams<ConstraintLayout.LayoutParams> {
            val orientation = resources.configuration.orientation
            dimensionRatio = viewModel.screenAspectRatio.getDimensionRatioString((orientation == Configuration.ORIENTATION_PORTRAIT))
        }

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().setTargetAspectRatio(viewModel.screenAspectRatio).build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                    .setFlashMode(imageCapture?.flashMode ?: ImageCapture.FLASH_MODE_AUTO)
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .setTargetAspectRatio(viewModel.screenAspectRatio).build()

            updateFlashView()
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, viewModel.lensFacing, preview, imageCapture)
            } catch (exc: Exception) {
//                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    fun flipCamera() {
        var rotationDegree = 0f
        if (viewModel.lensFacing === CameraSelector.DEFAULT_FRONT_CAMERA) {
            viewModel.lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
            rotationDegree = 180f
        } else if (viewModel.lensFacing === CameraSelector.DEFAULT_BACK_CAMERA) {
            viewModel.lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA
            rotationDegree = -180f
        }
        bindCameraUseCases()
        binding.ivFacing.animate().rotation(rotationDegree).setDuration(500).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    val flashClickListener = View.OnClickListener {
        binding.pnlRatioOptions.visibility = View.GONE
        binding.pnlFlashOptions.visibility = when (binding.pnlFlashOptions.visibility) {
            View.VISIBLE -> View.GONE
            View.GONE -> View.VISIBLE
            else -> binding.pnlFlashOptions.visibility
        }
    }

    val flashChangeListener = View.OnClickListener {
//        when (it.id) {
//            binding.ivFlashOff.id -> {
//                imageCapture?.flashMode = ImageCapture.FLASH_MODE_OFF
//                binding.pnlFlashOptions.visibility = View.GONE
//                updateFlashView()
//            }
//
//            binding.ivFlashOn.id -> {
//                imageCapture?.flashMode = ImageCapture.FLASH_MODE_ON
//                binding.pnlFlashOptions.visibility = View.GONE
//                updateFlashView()
//            }
//
//            binding.ivFlashAuto.id -> {
//                imageCapture?.flashMode = ImageCapture.FLASH_MODE_AUTO
//                binding.pnlFlashOptions.visibility = View.GONE
//                updateFlashView()
//            }
//        }
    }
    private fun captureView(view: View, window: Window, bitmapCallback: (Bitmap)->Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Above Android O, use PixelCopy
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val location = IntArray(2)
            view.getLocationInWindow(location)
            PixelCopy.request(
                    window,
                    Rect(
                            location[0],
                            location[1],
                            location[0] + view.width,
                            location[1] + view.height
                    ),
                    bitmap,
                    {
                        if (it == PixelCopy.SUCCESS) {
                            bitmapCallback.invoke(bitmap)
                        }
                    },
                    Handler(Looper.getMainLooper()) )
        } else {
            val tBitmap = Bitmap.createBitmap(
                    view.width, view.height, Bitmap.Config.RGB_565
            )
            val canvas = Canvas(tBitmap)
            view.draw(canvas)
            canvas.setBitmap(null)
            bitmapCallback.invoke(tBitmap)
        }
    }
    private fun updateFlashView() {
//        binding.ivFlash.setImageResource(
//            when (imageCapture?.flashMode) {
//                ImageCapture.FLASH_MODE_OFF -> {
//                    R.drawable.ic_flash_off
//                }
//
//                ImageCapture.FLASH_MODE_ON -> {
//                    R.drawable.ic_flash_on
//                }
//
//                ImageCapture.FLASH_MODE_AUTO -> {
//                    R.drawable.ic_flash_auto
//                }
//
//                else -> R.drawable.ic_flash_off
//            }
//        )
    }


    private val ratioClickListener = View.OnClickListener {
//        binding.pnlFlashOptions.visibility = View.GONE
//        binding.pnlRatioOptions.visibility = when (binding.pnlRatioOptions.visibility) {
//            View.VISIBLE -> View.GONE
//            View.GONE -> View.VISIBLE
//            else -> binding.pnlRatioOptions.visibility
//        }
    }


    private val facingChangeListener = View.OnClickListener {
        flipCamera()
    }

    private val zoomListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = camera?.cameraInfo?.zoomState?.value?.zoomRatio?.times(detector.scaleFactor)
            scale?.let { value ->
                camera?.cameraControl?.setZoomRatio(value)
                val result = if (value < 1.5) 0.0f else value
//                binding.zoomSeekBar.setOnSeekBarChangeListener(null)
//                binding.zoomSeekBar.progress = (result * 10).toInt()
//                binding.zoomSeekBar.setOnSeekBarChangeListener(zoomSeekListener)
            }

//            binding.zoomSeekWrapper.visibility = View.VISIBLE
            startTouchTimer()

            return true
        }
    }

    private val zoomSeekListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
            camera?.cameraControl?.setLinearZoom(progress / 100.toFloat())
            startTouchTimer()
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {

        }
    }

    private val ratioChangeListener = View.OnClickListener {
        var ratio = 0
        when (it.id) {
//            binding.tvRatio169.id -> {
//                ratio = AspectRatio.RATIO_16_9
//            }
//            binding.tvRatio43.id -> {
//                ratio = AspectRatio.RATIO_4_3
//            }
        }
        if (viewModel.screenAspectRatio != ratio) {
            viewModel.screenAspectRatio = ratio
            updateRatioView()
            lifecycleScope.launch {
                delay(500L)
                bindCameraUseCases()
            }
        }
//        binding.pnlRatioOptions.visibility = View.GONE
    }

    fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(
                Runnable {
                    // Used to bind the lifecycle of cameras to the lifecycle owner
                    cameraProvider = cameraProviderFuture.get()

                    // Get screen metrics used to setup camera for full screen resolution
                    val metrics = DisplayMetrics().also { binding.viewFinder.display.getRealMetrics(it) }
//                    Timber.d("Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

//                    val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
//                   Timber.d("Preview aspect ratio: $screenAspectRatio")

                    val rotation = binding.viewFinder.display.rotation

                    // CameraProvider
                    val cameraProvider = cameraProvider
                            ?: throw IllegalStateException("Camera initialization failed.")

                    // CameraSelector
                    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

                    // add text overlay *---------*
                    binding.viewFinder.overlay.add(binding.filter)

                    // Preview
                    preview = Preview.Builder()
                            // We request aspect ratio but no resolution
//                            .setTargetAspectRatio(RATIO_16_9)
                            // Set initial target rotation
                            .setTargetRotation(rotation)
                            .build()

                    // ImageCapture
                    imageCapture = ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            // We request aspect ratio but no resolution to match preview config, but letting
                            // CameraX optimize for whatever specific resolution best fits our use cases
//                            .setTargetAspectRatio()
                            // Set initial target rotation, we will have to call this again if rotation changes
                            // during the lifecycle of this use case
                            .setTargetRotation(rotation)
                            .build()

                    // Must unbind the use-cases before rebinding them
                    cameraProvider.unbindAll()

                    try {
                        // A variable number of use-cases can be passed here -
                        // camera provides access to CameraControl & CameraInfo
                        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                        // Attach the viewfinder's surface provider to preview use case
                        preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    } catch (exc: Exception) {
                        Toast.makeText(requireContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                },
                ContextCompat.getMainExecutor(requireContext())
        )
    }

//    private fun takePhoto() {
//        imageCapture?.let { imageCapture ->
//
//            // Create output file to hold the image
//            val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
//
//            // Setup image capture metadata
//            val metadata = ImageCapture.Metadata().apply {
//
//                // Mirror image when using the front camera
//                isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
//            }
//
//            // Create output options object which contains file + metadata
//            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
//                    .setMetadata(metadata)
//                    .build()
//
//            // Setup image capture listener which is triggered after photo has been taken
//            imageCapture.takePicture(outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exc: ImageCaptureException) {
//                    Timber.e(exc, "Photo capture failed: ${exc.message}")
//                }
//
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
//                    Timber.d("Photo capture succeeded: $savedUri")
//
//                    // Implicit broadcasts will be ignored for devices running API level >= 24
//                    // so if you only target API level 24+ you can remove this statement
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                        requireActivity()
//                                .sendBroadcast(Intent(android.hardware.Camera.ACTION_NEW_PICTURE, savedUri))
//                    }
//
//
//                    // If the folder selected is an external media directory, this is
//                    // unnecessary but otherwise other apps will not be able to access our
//                    // images unless we scan them using [MediaScannerConnection]
//                    val mimeType = MimeTypeMap.getSingleton()
//                            .getMimeTypeFromExtension(savedUri.toFile().extension)
//                    MediaScannerConnection.scanFile(
//                            context,
//                            arrayOf(savedUri.toFile().absolutePath),
//                            arrayOf(mimeType)
//                    ) { _, uri ->
//                        Timber.d("Image capture scanned into media store: $uri")
//                    }
//                }
//            })
//        }
//    }
//
//}

    private fun updateRatioView() {
        val orientation = resources.configuration.orientation
//        binding.tvRatio.text = viewModel.screenAspectRatio.getAspectRationString(orientation == Configuration.ORIENTATION_PORTRAIT)
//        binding.tvRatio169.text = if (orientation == Configuration.ORIENTATION_PORTRAIT) "9:16" else "16:9"
//        binding.tvRatio43.text = if (orientation == Configuration.ORIENTATION_PORTRAIT) "3:4" else "4:3"
    }

    private fun setPreview(uri: Uri) {
        binding.previewImage.setImageURI(uri)
        binding.pnlPreview.visibility = View.VISIBLE
        binding.pnlPreview.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ARG_PREVIEW_TYPE, MediaType.IMAGE)
            bundle.putString(ARG_MEDIA_PATH, uri.toString())
            findNavController().navigate(R.id.previewFragment, bundle)
            graphic_overlay
        }
    }

    private var timer: CountDownTimer? = null
    private fun startTouchTimer(duration: Long = 1000) {
        timer?.cancel()
        timer = null
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
            }
        }.start()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider {
                            preview
                        }
                    }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                        this as LifecycleOwner, cameraSelector, preview
                )

            } catch (exc: Exception) {
//                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this.requireContext()))
    }

//    private fun startCamera() {
////        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
//
//////        OpenCVLoader.initDebug()
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)
////
//        val cameraSelector =
//                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
//        cameraProviderFuture.addListener(Runnable {
//            // Used to bind the lifecycle of cameras to the lifecycle owner
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
////            // Preview
//            preview = Preview.Builder().build()
////
//            imageCapture = ImageCapture.Builder().build()
////
//            imageAnalyzer = ImageAnalysis.Builder().build().apply {
//                setAnalyzer(Executors.newSingleThreadExecutor(), ImageAnalysis.Analyzer {
////                setAnalyzer(Executors.newSingleThreadExecutor(), CornerAnalyzer {
////                    val bitmap = viewFinder.bitmap
////                    val img = Mat()
//                    PreferenceUtils.getObjectDetectorOptionsForLivePreview(context)
////                    PreferenceUtils.bitmapToMat(bitmap, img)
////                    bitmap?.recycle()
//                    // Do image analysis here if you need bitmap
//                })
//            }
//            // Select back camera
////
////            try {
////                // Unbind use cases before rebinding
////                cameraProvider.unbindAll()
////                cameraProvider.bindToLifecycle(
////                        this,
////                        cameraSelector,
////                        preview,
////                        imageCapture,
////                        imageAnalyzer
////                )
////                // Bind use cases to camera
////                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalyzer, preview, imageCapture)
////                preview?.setSurfaceProvider {
////                    viewFinder
////                }
////            } catch (exc: Exception) {
////                Log.e(CameraViewModel.TAG, "Use case binding failed", exc)
////            }
////
////        }, ContextCompat.getMainExecutor(safeContext))
////
////    }
//            cameraProviderFuture.addListener({
//                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//                val preview = Preview.Builder()
//                        .build()
//                        .also {
//                            it.setSurfaceProvider {
//                                preview
//                            }
//                        }
//                try {
//                    // Unbind use cases before rebinding
//                    cameraProvider.unbindAll()
//                    cameraProvider.bindToLifecycle(
//                            this,
//                            cameraSelector,
//                            preview,
//                            imageCapture,
//                            imageAnalyzer
//                    )
//                    // Bind use cases to camera
//                    camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalyzer, preview, imageCapture)
//                    preview?.setSurfaceProvider {
//                        viewFinder
//                    }
////            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
////            try {
////                cameraProvider.unbindAll()
////
////                cameraProvider.bindToLifecycle(
////                        this as LifecycleOwner, cameraSelector, preview
////                )
////
//                } catch (exc: Exception) {
////                Log.e(TAG, "Use case binding failed", exc)
//                }
//
//            }, ContextCompat.getMainExecutor(safeContext))
//        }





    fun runObjectDetection(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)

        val options = ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build()
        val objectDetector = ObjectDetection.getClient(options)

        objectDetector.process(image).addOnSuccessListener { results ->
            debugPrint(results)

            // Parse ML Kit's DetectedObject and create corresponding visualization data
            val detectedObjects = results.map {
                var text = "Unknown"

                // We will show the top confident detection result if it exist
                if (it.labels.isNotEmpty()) {
                    val firstLabel = it.labels.first()
                    text = "${firstLabel.text}, ${firstLabel.confidence.times(100).toInt()}%"
                }
                BoxWithText(it.boundingBox, text)
            }

            // Draw the detection result on the input bitmap
            val visualizedResult = drawDetectionResult(bitmap, detectedObjects)

            // Show the detection result on the app screen
            inputImageView.setImageBitmap(visualizedResult)
        }.addOnFailureListener {
            Log.e(CameraViewModel.TAG, it.message.toString())
        }
    }

    private fun setViewAndDetect(bitmap: Bitmap) {
        // Display the captured image
        inputImageView.setImageBitmap(bitmap)
        tvPlaceholder.visibility = View.VISIBLE

        // Run object detection and display the result
        runObjectDetection(bitmap)
    }

    /**
     * getCapturedImage():
     *     Decodes and crops the captured image from camera.
     */
    private fun getCapturedImage(): Bitmap {
        // Get the dimensions of the View
        val targetW: Int = inputImageView.width
        val targetH: Int = inputImageView.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(currentPhotoPath, this)

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = max(1, min(photoW / targetW, photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inMutable = true
        }
        val exifInterface = ExifInterface(currentPhotoPath)
        val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
        )

        val bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotateImage(bitmap, 90f)
            }

            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotateImage(bitmap, 180f)
            }

            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotateImage(bitmap, 270f)
            }

            else -> {
                bitmap
            }
        }
    }

    /**
     * Get image form drawable and convert to bitmap.
     */
    private fun getSampleImage(drawable: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, drawable, BitmapFactory.Options().apply {
            inMutable = true
        })

    }

    /**
     * Rotate the given bitmap.
     */
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
                source, 20, 20, source.width, source.height,
                matrix, true
        )
    }

    /**
     * Create a file to pass to a camera app for storing captured image.
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg",
                /* suffix */
//            storageDir /* directory */
        ).apply {
//             Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
//            takePictureIntent.resolveActivity(packageManager)?.also {
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile()
            } catch (e: IOException) {
//                Log.e(CameraXLivePreviewFragment.TAG, e.message.toString())
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                        this.requireContext(),
                        "com.google.mlkit.codelab.objectdetection.fileprovider",
                        it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }


   fun drawDetectionResult(
            bitmap: Bitmap,
            detectionResults: List<BoxWithText>
    ): Bitmap {
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = android.graphics.Canvas(outputBitmap)
        val pen = Paint()
        pen.textAlign = Paint.Align.LEFT

        detectionResults.forEach {
            // draw bounding box
            pen.color = Color.RED
            pen.strokeWidth = 8F
            pen.style = Paint.Style.STROKE
            val box = it.box
            canvas.drawRect(box, pen)

            val tagSize = Rect(10, 10, 10, 10)

            // calculate the right font size
            pen.style = Paint.Style.FILL_AND_STROKE
            pen.color = Color.YELLOW
            pen.strokeWidth = 20F

//            pen.textSize =
            pen.getTextBounds(it.text, 0, it.text.length, tagSize)
            val fontSize: Float = pen.textSize * box.width() / tagSize.width()

            // adjust the font size so texts are inside the bounding box
            if (fontSize < pen.textSize) pen.textSize = fontSize

            var margin = (box.width() - tagSize.width()) / 2.0F
            if (margin < 0F) margin = 0F
            canvas.drawText(
                    it.text, box.left + margin,
                    box.top + tagSize.height().times(1F), pen
            )
        }
        return outputBitmap
    }

    /**
     * Print out the object detection result to Logcat.
     */
    private fun debugPrint(detectedObjects: List<DetectedObject>) {
        detectedObjects.forEachIndexed { index, detectedObject ->
            val box = detectedObject.boundingBox

            Log.d(VideoFragment.TAG, "Detected object: $index")
            Log.d(VideoFragment.TAG, " trackingId: ${detectedObject.trackingId}")
            Log.d(VideoFragment.TAG, " boundingBox: (${box.left}, ${box.top}) - (${box.right},${box.bottom})")
            detectedObject.labels.forEach {
                Log.d(VideoFragment.TAG, " categories: ${it.text}")
                Log.d(VideoFragment.TAG, " confidence: ${it.confidence}")
            }
        }
    }
}

    /**
     * A general-purpose data class to store detection result for visualization
     */





//class CameraFragment : Fragment() {
//    lateinit var cameraExecutor: ExecutorService
//    lateinit var scaleGestureDetector: ScaleGestureDetector
//    lateinit var binding: FragmentCameraBinding
//    var imageCapture: ImageCapture? = null
//    var camera: Camera? = null
//    val viewModel: CameraViewModel by viewModels()
//    lateinit var result: String
//
//    private lateinit var captureImageFab: Button
//    private lateinit var inputImageView: ImageView
//    private lateinit var imgSampleOne: ImageView
//    private lateinit var imgSampleTwo: ImageView
//    private lateinit var imgSampleThree: ImageView
//    private lateinit var tvPlaceholder: TextView
//    private lateinit var currentPhotoPath: String
//    private lateinit var cameraManager: CameraManager
//
//    private val filterListener = View.OnClickListener {
//        changeFilter()
//    }
//    private fun onClicks() {
////        btnSwitch.setOnClickListener {
////            cameraManager.changeCameraSelector()
////        }
//    }
//    private fun createCameraManager() {
////        cameraManager = CameraManager(
//////            this.requireContext()
//////            preview,
//////            this,
//////            graphicOverlay_finder,
//////            ::processPicture
////        )
//    }
//    private fun processPicture(faceStatus: FaceStatus) {
//        Log.e("facestatus","This is it ${faceStatus.name}")
////       when(faceStatus){}
//    }
//
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = FragmentCameraBinding.inflate(inflater, container, false)
////        btn = (Button) view.findViewById(R.id.cameraButton)
////        String text = getA
//        createCameraManager()
//
//        return binding.root
//
//
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.viewFinder.setOnTouchListener { _, motionEvent ->
//            scaleGestureDetector.onTouchEvent(motionEvent)
//            when (motionEvent.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    binding.pnlFlashOptions.visibility = View.GONE
//                    binding.pnlRatioOptions.visibility = View.GONE
//                    startTouchTimer()
//                    return@setOnTouchListener true
//                }
//                MotionEvent.ACTION_UP -> {
//                    val factory = binding.viewFinder.meteringPointFactory
//                    val point = factory.createPoint(motionEvent.x, motionEvent.y)
//                    val action = FocusMeteringAction.Builder(point).build()
//                    camera?.cameraControl?.startFocusAndMetering(action)
//                    return@setOnTouchListener true
//                }
//                else -> return@setOnTouchListener false
//            }
//        }
//
////        captureImageFab = findViewById(R.id.captureImageFab)
////        inputImageView = findViewById(R.id.imageView)
////        imgSampleOne = findViewById(R.id.imgSampleOne)
////        imgSampleTwo = findViewById(R.id.imgSampleTwo)
////        imgSampleThree = findViewById(R.id.imgSampleThree)
////        tvPlaceholder = findViewById(R.id.tvPlaceholder)
////
////        captureImageFab.setOnClickListener(this)
////        imgSampleOne.setOnClickListener(this)
////        imgSampleTwo.setOnClickListener(this)
////        imgSampleThree.setOnClickListener(this)
//
//        updateRatioView()
////        binding.scrollView.setOnClickListener {
////
////        }
////        binding.filterOne.setOnClickListener {
////
////        }
////        binding.filterTwo.setOnClickListener {
////            binding.filterTwo.text = "Filter2"
////        }
////        binding.filterThree.setOnClickListener {
////            binding.filterThree.setText("Filter3")
////        }
////        binding.filterThree.setOnClickListener { changeFilter() }
//        binding.photoButton.setOnClickListener { takePhoto() }
//        binding.pnlFLash.setOnClickListener(flashClickListener)
////        binding.ivFlashOff.setOnClickListener(flashChangeListener)
////        binding.ivFlashOn.setOnClickListener(flashChangeListener)
////        binding.ivFlashAuto.setOnClickListener(flashChangeListener)
//        binding.pnlFacing.setOnClickListener(facingChangeListener)
//        binding.pnlRatio.setOnClickListener(ratioClickListener)
//        binding.tvRatio169.setOnClickListener(ratioChangeListener)
//        binding.tvRatio43.setOnClickListener(ratioChangeListener)
//        scaleGestureDetector = ScaleGestureDetector(requireContext(), zoomListener)
////        binding.swCameraOption.setOnCheckedChangeListener { _, isChecked ->
////            if (isChecked) {
////                findNavController().navigate(R.id.previewFragment)
////            }
////        }
//        cameraExecutor = Executors.newSingleThreadExecutor()
//        bindCameraUseCases()
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        updateRatioView()
//    }
//
//    fun changeFilter() {
////        binding.filterOne.text = "Filter one"
//        binding.filter.text = result
////        binding.cameraResult
//        if (result.isEmpty()) {
//            val result = "Evil"
//        }
//
//    }
//
//    fun takePhoto() {
//        binding.captureProgress.visibility = View.VISIBLE
//        val imageCapture = imageCapture ?: return
//        imageCapture.takePicture(
//            OutputFileOptionsFactory().getPhotoOutputFileOption(requireActivity()),
//            ContextCompat.getMainExecutor(requireContext()),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(exc: ImageCaptureException) {
//                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
//                    viewModel.savedUri.value
//                }
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    output.savedUri?.let { setPreview(it) }
//                    viewModel.savedUri.value = output.savedUri
//                    binding.captureProgress.visibility = View.VISIBLE
//                }
//            })
//    }
//
//    fun bindCameraUseCases() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
//        binding.viewFinder.updateLayoutParams<ConstraintLayout.LayoutParams> {
//            val orientation = resources.configuration.orientation
//            dimensionRatio = viewModel.screenAspectRatio.getDimensionRatioString((orientation == Configuration.ORIENTATION_PORTRAIT))
//        }
//
//        cameraProviderFuture.addListener({
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//            val preview = Preview.Builder().setTargetAspectRatio(viewModel.screenAspectRatio).build().also {
//                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
//            }
//
//            imageCapture = ImageCapture.Builder()
//                .setFlashMode(imageCapture?.flashMode ?: ImageCapture.FLASH_MODE_AUTO)
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
//                .setTargetAspectRatio(viewModel.screenAspectRatio).build()
//
//            updateFlashView()
//            try {
//                cameraProvider.unbindAll()
//                camera = cameraProvider.bindToLifecycle(this, viewModel.lensFacing, preview, imageCapture)
//            } catch (exc: Exception) {
//                Log.e(TAG, "Use case binding failed", exc)
//            }
//        }, ContextCompat.getMainExecutor(requireContext()))
//    }
//
//    fun flipCamera() {
//        var rotationDegree = 0f
//        if (viewModel.lensFacing === CameraSelector.DEFAULT_FRONT_CAMERA) {
//            viewModel.lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
//            rotationDegree = 180f
//        } else if (viewModel.lensFacing === CameraSelector.DEFAULT_BACK_CAMERA) {
//            viewModel.lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA
//            rotationDegree = -180f
//        }
//        bindCameraUseCases()
//        binding.ivFacing.animate().rotation(rotationDegree).setDuration(500).start()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraExecutor.shutdown()
//    }
//
//    val flashClickListener = View.OnClickListener {
//        binding.pnlRatioOptions.visibility = View.GONE
//        binding.pnlFlashOptions.visibility = when (binding.pnlFlashOptions.visibility) {
//            View.VISIBLE -> View.GONE
//            View.GONE -> View.VISIBLE
//            else -> binding.pnlFlashOptions.visibility
//        }
//    }
//
//    val flashChangeListener = View.OnClickListener {
////        when (it.id) {
////            binding.ivFlashOff.id -> {
////                imageCapture?.flashMode = ImageCapture.FLASH_MODE_OFF
////                binding.pnlFlashOptions.visibility = View.GONE
////                updateFlashView()
////            }
////
////            binding.ivFlashOn.id -> {
////                imageCapture?.flashMode = ImageCapture.FLASH_MODE_ON
////                binding.pnlFlashOptions.visibility = View.GONE
////                updateFlashView()
////            }
////
////            binding.ivFlashAuto.id -> {
////                imageCapture?.flashMode = ImageCapture.FLASH_MODE_AUTO
////                binding.pnlFlashOptions.visibility = View.GONE
////                updateFlashView()
////            }
////        }
//    }
//
//    private fun updateFlashView() {
////        binding.ivFlash.setImageResource(
////            when (imageCapture?.flashMode) {
////                ImageCapture.FLASH_MODE_OFF -> {
////                    R.drawable.ic_flash_off
////                }
////
////                ImageCapture.FLASH_MODE_ON -> {
////                    R.drawable.ic_flash_on
////                }
////
////                ImageCapture.FLASH_MODE_AUTO -> {
////                    R.drawable.ic_flash_auto
////                }
////
////                else -> R.drawable.ic_flash_off
////            }
////        )
//    }
//
//
//    private val ratioClickListener = View.OnClickListener {
//        binding.pnlFlashOptions.visibility = View.GONE
//        binding.pnlRatioOptions.visibility = when (binding.pnlRatioOptions.visibility) {
//            View.VISIBLE -> View.GONE
//            View.GONE -> View.VISIBLE
//            else -> binding.pnlRatioOptions.visibility
//        }
//    }
//
//
//    private val facingChangeListener = View.OnClickListener {
//        flipCamera()
//    }
//
//    private val zoomListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//        override fun onScale(detector: ScaleGestureDetector): Boolean {
//            val scale = camera?.cameraInfo?.zoomState?.value?.zoomRatio?.times(detector.scaleFactor)
//            scale?.let { value ->
//                camera?.cameraControl?.setZoomRatio(value)
//                val result = if (value < 1.5) 0.0f else value
////                binding.zoomSeekBar.setOnSeekBarChangeListener(null)
////                binding.zoomSeekBar.progress = (result * 10).toInt()
////                binding.zoomSeekBar.setOnSeekBarChangeListener(zoomSeekListener)
//            }
//
////            binding.zoomSeekWrapper.visibility = View.VISIBLE
//            startTouchTimer()
//
//            return true
//        }
//    }
//
//    private val zoomSeekListener = object : SeekBar.OnSeekBarChangeListener {
//        override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
//            camera?.cameraControl?.setLinearZoom(progress / 100.toFloat())
//            startTouchTimer()
//        }
//
//        override fun onStartTrackingTouch(p0: SeekBar?) {
//
//        }
//
//        override fun onStopTrackingTouch(p0: SeekBar?) {
//
//        }
//    }
//
//    private val ratioChangeListener = View.OnClickListener {
//        var ratio = 0
//        when (it.id) {
//            binding.tvRatio169.id -> {
//                ratio = AspectRatio.RATIO_16_9
//            }
//            binding.tvRatio43.id -> {
//                ratio = AspectRatio.RATIO_4_3
//            }
//        }
//        if (viewModel.screenAspectRatio != ratio) {
//            viewModel.screenAspectRatio = ratio
//            updateRatioView()
//            lifecycleScope.launch {
//                delay(500L)
//                bindCameraUseCases()
//            }
//        }
//        binding.pnlRatioOptions.visibility = View.GONE
//    }
//    private fun updateRatioView() {
//        val orientation = resources.configuration.orientation
////        binding.tvRatio.text = viewModel.screenAspectRatio.getAspectRationString(orientation == Configuration.ORIENTATION_PORTRAIT)
////        binding.tvRatio169.text = if (orientation == Configuration.ORIENTATION_PORTRAIT) "9:16" else "16:9"
////        binding.tvRatio43.text = if (orientation == Configuration.ORIENTATION_PORTRAIT) "3:4" else "4:3"
//    }
//
//    private fun setPreview(uri: Uri) {
//        binding.previewImage.setImageURI(uri)
//        binding.pnlPreview.visibility = View.VISIBLE
//        binding.pnlPreview.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString(ARG_PREVIEW_TYPE, MediaType.IMAGE)
//            bundle.putString(ARG_MEDIA_PATH, uri.toString())
//            findNavController().navigate(R.id.previewFragment, bundle)
//        }
//    }
//    private var timer: CountDownTimer? = null
//    private fun startTouchTimer(duration: Long = 1000) {
//        timer?.cancel()
//        timer = null
//        timer = object : CountDownTimer(duration, 1000) {
//            override fun onTick(millisUntilFinished: Long) {}
//            override fun onFinish() {
//            }
//        }.start()
//    }
//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
//
//        cameraProviderFuture.addListener({
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider {
//                        preview
//                    }
//                }
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            try {
//                cameraProvider.unbindAll()
//
//                cameraProvider.bindToLifecycle(
//                    this as LifecycleOwner, cameraSelector, preview
//                )
//
//            } catch (exc: Exception) {
//                Log.e(TAG, "Use case binding failed", exc)
//            }
//
//        }, ContextCompat.getMainExecutor(this.requireContext()))
//    }
//
//    private fun runObjectDetection(bitmap: Bitmap) {
//        val image = InputImage.fromBitmap(bitmap, 0)
//
//        val options = ObjectDetectorOptions.Builder()
//            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
//            .enableMultipleObjects()
//            .enableClassification()
//            .build()
//        val objectDetector = ObjectDetection.getClient(options)
//
//        objectDetector.process(image).addOnSuccessListener { results ->
//            debugPrint(results)
//
//            // Parse ML Kit's DetectedObject and create corresponding visualization data
//            val detectedObjects = results.map {
//                var text = "Unknown"
//
//                // We will show the top confident detection result if it exist
//                if (it.labels.isNotEmpty()) {
//                    val firstLabel = it.labels.first()
//                    text = "${firstLabel.text}, ${firstLabel.confidence.times(100).toInt()}%"
//                }
////                BoxWithText(it.boundingBox, text)
//            }
//
//            // Draw the detection result on the input bitmap
////            val visualizedResult = drawDetectionResult(bitmap, detectionResults)
//
//            // Show the detection result on the app screen
////            inputImageView.setImageBitmap(visualizedResult)
//        }.addOnFailureListener {
//            Log.e(CameraViewModel.TAG, it.message.toString())
//        }
//    }
//    private fun setViewAndDetect(bitmap: Bitmap) {
//        // Display the captured image
//        inputImageView.setImageBitmap(bitmap)
//        tvPlaceholder.visibility = View.INVISIBLE
//
//        // Run object detection and display the result
//        runObjectDetection(bitmap)
//    }
//
//    /**
//     * getCapturedImage():
//     *     Decodes and crops the captured image from camera.
//     */
//    private fun getCapturedImage(): Bitmap {
//        // Get the dimensions of the View
//        val targetW: Int = inputImageView.width
//        val targetH: Int = inputImageView.height
//
//        val bmOptions = BitmapFactory.Options().apply {
//            // Get the dimensions of the bitmap
//            inJustDecodeBounds = true
//
//            BitmapFactory.decodeFile(currentPhotoPath, this)
//
//            val photoW: Int = outWidth
//            val photoH: Int = outHeight
//
//            // Determine how much to scale down the image
//            val scaleFactor: Int = max(1, min(photoW / targetW, photoH / targetH))
//
//            // Decode the image file into a Bitmap sized to fill the View
//            inJustDecodeBounds = false
//            inSampleSize = scaleFactor
//            inMutable = true
//        }
//        val exifInterface = ExifInterface(currentPhotoPath)
//        val orientation = exifInterface.getAttributeInt(
//            ExifInterface.TAG_ORIENTATION,
//            ExifInterface.ORIENTATION_UNDEFINED
//        )
//
//        val bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
//        return when (orientation) {
//            ExifInterface.ORIENTATION_ROTATE_90 -> {
//                rotateImage(bitmap, 90f)
//            }
//            ExifInterface.ORIENTATION_ROTATE_180 -> {
//                rotateImage(bitmap, 180f)
//            }
//            ExifInterface.ORIENTATION_ROTATE_270 -> {
//                rotateImage(bitmap, 270f)
//            }
//            else -> {
//                bitmap
//            }
//        }
//    }
//
//    /**
//     * Get image form drawable and convert to bitmap.
//     */
//    private fun getSampleImage(drawable: Int): Bitmap {
//        return BitmapFactory.decodeResource(resources, drawable, BitmapFactory.Options().apply {
//            inMutable = true
//        })
//
//}
//
///**
// * Rotate the given bitmap.
// */
//private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
//    val matrix = Matrix()
//    matrix.postRotate(angle)
//    return Bitmap.createBitmap(
//        source, 0, 0, source.width, source.height,
//        matrix, true
//    )
//}
//
///**
// * Create a file to pass to a camera app for storing captured image.
// */
//@Throws(IOException::class)
//private fun createImageFile(): File {
//    // Create an image file name
//    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
////        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//    return File.createTempFile(
//        "JPEG_${timeStamp}_", /* prefix */
//        ".jpg", /* suffix */
////            storageDir /* directory */
//    ).apply {
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = absolutePath
//    }
//}
//private fun dispatchTakePictureIntent() {
//    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//        // Ensure that there's a camera activity to handle the intent
////            takePictureIntent.resolveActivity(packageManager)?.also {
//        // Create the File where the photo should go
//        val photoFile: File? = try {
//            createImageFile()
//        } catch (e: IOException) {
//            Log.e(VideoFragment.TAG, e.message.toString())
//            null
//        }
//        // Continue only if the File was successfully created
//        photoFile?.also {
//            val photoURI: Uri = FileProvider.getUriForFile(
//                this.requireContext(),
//                "com.google.mlkit.codelab.objectdetection.fileprovider",
//                it
//            )
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
////                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//        }
//    }
//}
//
//
///**
// * Draw bounding boxes around objects together with the object's name.
// */
//private fun drawDetectionResult(
//    bitmap: Bitmap,
//    detectionResults: List<BoxWithText>
//): Bitmap {
//    val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
//    val canvas = android.graphics.Canvas(outputBitmap)
//    val pen = Paint()
//    pen.textAlign = Paint.Align.LEFT
//
//    detectionResults.forEach {
//        // draw bounding box
//        pen.color = Color.RED
//        pen.strokeWidth = 8F
//        pen.style = Paint.Style.STROKE
//        val box = it.box
//        canvas.drawRect(box, pen)
//
//        val tagSize = Rect(0, 0, 0, 0)
//
//        // calculate the right font size
//        pen.style = Paint.Style.FILL_AND_STROKE
//        pen.color = Color.YELLOW
//        pen.strokeWidth = 2F
//
////            pen.textSize =
//        pen.getTextBounds(it.text, 0, it.text.length, tagSize)
//        val fontSize: Float = pen.textSize * box.width() / tagSize.width()
//
//        // adjust the font size so texts are inside the bounding box
//        if (fontSize < pen.textSize) pen.textSize = fontSize
//
//        var margin = (box.width() - tagSize.width()) / 2.0F
//        if (margin < 0F) margin = 0F
//        canvas.drawText(
//            it.text, box.left + margin,
//            box.top + tagSize.height().times(1F), pen
//        )
//    }
//    return outputBitmap
//}
//
///**
// * Print out the object detection result to Logcat.
// */
//private fun debugPrint(detectedObjects: List<DetectedObject>) {
//    detectedObjects.forEachIndexed { index, detectedObject ->
//        val box = detectedObject.boundingBox
//
//        Log.d(VideoFragment.TAG, "Detected object: $index")
//        Log.d(VideoFragment.TAG, " trackingId: ${detectedObject.trackingId}")
//        Log.d(VideoFragment.TAG, " boundingBox: (${box.left}, ${box.top}) - (${box.right},${box.bottom})")
//        detectedObject.labels.forEach {
//            Log.d(VideoFragment.TAG, " categories: ${it.text}")
//            Log.d(VideoFragment.TAG, " confidence: ${it.confidence}")
//        }
//    }
//}
///**
// * A general-purpose data class to store detection result for visualization
// */
//
//data class BoxWithText(val box: Rect, val text: String)
//
//
////companion object {
////    private const val TAG = "CameraX"
////}
////    companion object {
////        private const val TAG = "YourCameraActivity"
////        private const val REQUEST_CODE_PERMISSIONS = 10
////        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
////    }
//
////    override fun onCreateView(
////        inflater: LayoutInflater,
////        container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        binding = FragmentCameraBinding.inflate(inflater, container, false)
////        return binding.root
////    }
////
////    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
////        super.onViewCreated(view, savedInstanceState)
////
////    }
////
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////
//////    val binding: FragmentCameraBinding by lazy {
//////        FragmentCameraBinding.inflate(layoutInflater)
//////    }
//////binding = FragmentCameraBinding.inflate(layoutInflater)
////
//////binding = FragmentCameraBinding.inflate(layoutInflater)
//////        binding.viewModel = viewModel
////
////        // Initialize CameraX
////        if (allPermissionsGranted()) {
////            startCamera()
////        } else {
////            ActivityCompat.requestPermissions(
////                this.requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
////            )
////        }
////
////        val imageCapture = ImageCapture.Builder().build()
////
////        val orientationEventListener = object : OrientationEventListener(this as Context) {
////            override fun onOrientationChanged(orientation : Int) {
////                // Monitors orientation values to determine the target rotation value
////                val rotation : Int = when (orientation) {
////                    in 45..134 -> Surface.ROTATION_270
////                    in 135..224 -> Surface.ROTATION_180
////                    in 225..314 -> Surface.ROTATION_90
////                    else -> Surface.ROTATION_0
////                }
////
////                imageCapture.targetRotation = rotation
////            }
////        }
////        orientationEventListener.enable()
////    }
////
////
////    private fun allPermissionsGranted(): Boolean {
////        // Check for camera permissions
////        for (permission in REQUIRED_PERMISSIONS) {
////            if (ContextCompat.checkSelfPermission(
////                    this.requireContext(), permission
////                ) != PackageManager.PERMISSION_GRANTED
////            ) {
////                return false
////            }
////        }
////        return true
////
////    }
////
////
////    private fun startCamera() {
////        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
////
////        cameraProviderFuture.addListener({
////            // CameraProvider is now guaranteed to be available
////            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
////
////            // Set up the preview use case
////            val preview = Preview.Builder()
////                .build()
////                .also {
////                    it.setSurfaceProvider {
////                        preview
////                    }
////                }
////
////            // Select back camera
////            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
////
////            try {
////                cameraProvider.unbindAll()
////
////                cameraProvider.bindToLifecycle(
////                    this as LifecycleOwner, cameraSelector, preview
////                )
////
////            } catch (exc: Exception) {
////                Log.e(TAG, "Use case binding failed", exc)
////            }
////
////        }, ContextCompat.getMainExecutor(this.requireContext()))
////    }
////
////    // Handle permission request response
////    override fun onRequestPermissionsResult(
////        requestCode: Int, permissions: Array<String>, grantResults: IntArray
////    ) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
////        if (requestCode == REQUEST_CODE_PERMISSIONS) {
////            if (allPermissionsGranted()) {
////                startCamera()
////            } else {
////
//////                Toast.makeText(
//////                    this,
//////                    "Permissions not granted by the user.",
//////                    Toast.LENGTH_SHORT
//////                ).show()
////            }
////        }
////    }
////
//////    private val multiplePermissionId = 14
//////    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
//////        arrayListOf(
//////            android.Manifest.permission.CAMERA,
//////            android.Manifest.permission.RECORD_AUDIO
//////        )
//////    } else {
//////        arrayListOf(
//////            android.Manifest.permission.CAMERA,
//////            android.Manifest.permission.RECORD_AUDIO,
//////            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//////        )
//////    }
//////
//////    private lateinit var videoCapture: VideoCapture<Recorder>
//////    private var recording: Recording? = null
//////
//////    private var isPhoto = true
//////
//////    private lateinit var imageCapture: ImageCapture
//////    private lateinit var cameraProvider: ProcessCameraProvider
//////    private lateinit var camera: Camera
//////    private lateinit var cameraSelector: CameraSelector
//////    private var orientationEventListener: OrientationEventListener? = null
//////    private var lensFacing = CameraSelector.LENS_FACING_BACK
//////    private var aspectRatio = AspectRatio.RATIO_16_9
//////
//////
//////    override fun onCreate(savedInstanceState: Bundle?) {
//////        super.onCreate(savedInstanceState)
////////      binding.root
////////        setContentView(binding.root)
//////
//////
//////        if (checkMultiplePermission()) {
//////            startCamera()
//////        }
//////
//////         binding.flipCameraIB.setOnClickListener {
//////            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
//////                CameraSelector.LENS_FACING_BACK
//////            } else {
//////                CameraSelector.LENS_FACING_FRONT
//////            }
//////            bindCameraUserCases()
//////        }
//////        binding.aspectRatioTxt.setOnClickListener {
//////            if (aspectRatio == AspectRatio.RATIO_16_9) {
//////                aspectRatio = AspectRatio.RATIO_4_3
//////                setAspectRatio("H,4:3")
//////               binding.aspectRatioTxt.text = "4:3"
//////            } else {
//////                aspectRatio = AspectRatio.RATIO_16_9
//////                setAspectRatio("H,0:0")
//////             binding.aspectRatioTxt.text = "16:9"
//////            }
//////            bindCameraUserCases()
//////        }
//////        binding.changeCameraToVideoIB.setOnClickListener {
//////            isPhoto = !isPhoto
//////            if (isPhoto){
//////                binding.changeCameraToVideoIB.setImageResource(R.drawable.ic_photo)
//////                binding.captureIB.setImageResource(R.drawable.camera)
//////            }else{
//////               binding.changeCameraToVideoIB.setImageResource(R.drawable.ic_videocam)
//////                binding.captureIB.setImageResource(R.drawable.ic_start)
////            }
////
////        }
////
////      binding.captureIB.setOnClickListener {
////            if (isPhoto) {
////                takePhoto()
////            }else{
////                captureVideo()
////            }
////        }
////       binding.flashToggleIB.setOnClickListener {
////            setFlashIcon(camera)
////        }
////
////    }
////
////
////    private fun checkMultiplePermission(): Boolean {
////        val listPermissionNeeded = arrayListOf<String>()
////        for (permission in multiplePermissionNameList) {
////            if (ContextCompat.checkSelfPermission(
////                    this.requireContext(),
////                    permission
////                ) != PackageManager.PERMISSION_GRANTED
////            ) {
////                listPermissionNeeded.add(permission)
////            }
////        }
////        if (listPermissionNeeded.isNotEmpty()) {
////            ActivityCompat.requestPermissions(
////                this.requireActivity(),
////                listPermissionNeeded.toTypedArray(),
////                multiplePermissionId
////            )
////            return false
////        }
////        return true
////    }
////
////    override fun onRequestPermissionsResult(
////        requestCode: Int,
////        permissions: Array<out String>,
////        grantResults: IntArray,
////    ) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
////
////        if (requestCode == multiplePermissionId) {
////            if (grantResults.isNotEmpty()) {
////                var isGrant = true
////                for (element in grantResults) {
////                    if (element == PackageManager.PERMISSION_DENIED) {
////                        isGrant = false
////                    }
////                }
////                if (isGrant) {
////                    // here all permission granted successfully
////                    startCamera()
////                } else {
////                    var someDenied = false
////                    for (permission in permissions) {
////                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
////                                this.requireActivity(),
////                                permission
////                            )
////                        ) {
////                            if (ActivityCompat.checkSelfPermission(
////                                    this.requireActivity(),
////                                    permission
////                                ) == PackageManager.PERMISSION_DENIED
////                            ) {
////                                someDenied = true
////                            }
////                        }
////                    }
//////                    if (someDenied) {
//////                        // here app Setting open because all permission is not granted
//////                        // and permanent denied
//////                        appSettingOpen(this)
//////                    } else {
//////                        // here warning permission show
//////
//////                        warningPermissionDialog(this) { _: DialogInterface, which: Int ->
//////                            when (which) {
//////                                DialogInterface.BUTTON_POSITIVE ->
//////                                    checkMultiplePermission()
//////                            }
//////                        }
//////                    }
////                }
////            }
////        }
////    }
////
////    private fun startCamera() {
////        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
////        cameraProviderFuture.addListener({
////            cameraProvider = cameraProviderFuture.get()
////            bindCameraUserCases()
////        }, ContextCompat.getMainExecutor(this.requireContext()))
////    }
////
////
////    private fun bindCameraUserCases() {
////        val rotation = binding.previewView.display.rotation
////
////        val resolutionSelector = ResolutionSelector.Builder()
////            .setAspectRatioStrategy(
////                AspectRatioStrategy(
////                    aspectRatio,
////                    AspectRatioStrategy.FALLBACK_RULE_AUTO
////                )
////            )
////            .build()
////
////        val preview = Preview.Builder()
////            .setResolutionSelector(resolutionSelector)
////            .setTargetRotation(rotation)
////            .build()
////            .also {
////                it.setSurfaceProvider(binding.previewView.surfaceProvider)
////            }
////
////        val recorder = Recorder.Builder()
////            .setQualitySelector(
////                QualitySelector.from(
////                    Quality.HIGHEST,
////                    FallbackStrategy.higherQualityOrLowerThan(Quality.SD)
////                )
////            )
////            .setAspectRatio(aspectRatio)
////            .build()
////
////        videoCapture = VideoCapture.withOutput(recorder).apply {
////            targetRotation = rotation
////        }
////
////        imageCapture = ImageCapture.Builder()
////            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
////            .setResolutionSelector(resolutionSelector)
////            .setTargetRotation(rotation)
////            .build()
////
////        cameraSelector = CameraSelector.Builder()
////            .requireLensFacing(lensFacing)
////            .build()
////
////        orientationEventListener = object : OrientationEventListener(this.requireContext()) {
////            override fun onOrientationChanged(orientation: Int) {
////                // Monitors orientation values to determine the target rotation value
////                val myRotation = when (orientation) {
////                    in 45..134 -> Surface.ROTATION_270
////                    in 135..224 -> Surface.ROTATION_180
////                    in 225..314 -> Surface.ROTATION_90
////                    else -> Surface.ROTATION_0
////                }
////                imageCapture.targetRotation = myRotation
////                videoCapture.targetRotation = myRotation
////            }
////        }
////        orientationEventListener?.enable()
////
////        try {
////            cameraProvider.unbindAll()
////
////            camera = cameraProvider.bindToLifecycle(
////                this, cameraSelector, preview, imageCapture, videoCapture
////            )
////            setUpZoomTapToFocus()
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
////    }
////
////    private fun setUpZoomTapToFocus() {
////        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
////            override fun onScale(detector: ScaleGestureDetector): Boolean {
////                val currentZoomRatio = camera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
////                val delta = detector.scaleFactor
////                camera.cameraControl.setZoomRatio(currentZoomRatio * delta)
////                return true
////            }
////        }
////        val scaleGestureDetector = ScaleGestureDetector(this.requireContext(), listener)
////
////        binding.previewView.setOnTouchListener { view, event ->
////            scaleGestureDetector.onTouchEvent(event)
////            if (event.action == MotionEvent.ACTION_DOWN) {
////                val factory = binding.previewView.meteringPointFactory
////                val point = factory.createPoint(event.x, event.y)
////                val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
////                    .setAutoCancelDuration(2, TimeUnit.SECONDS)
////                    .build()
////
////                val x = event.x
////                val y = event.y
////
////                val focusCircle = RectF(x - 50, y - 50, x + 50, y + 50)
////
////                binding.focusCircleView.focusCircle = focusCircle
////                binding.focusCircleView.invalidate()
////
////                camera.cameraControl.startFocusAndMetering(action)
////
////                view.performClick()
////            }
////            true
////        }
////    }
////
////    private fun setFlashIcon(camera: Camera) {
////        if (camera.cameraInfo.hasFlashUnit()) {
////            if (camera.cameraInfo.torchState.value == 0) {
////                camera.cameraControl.enableTorch(true)
////                binding.flashToggleIB.setImageResource(R.drawable.flash_off)
////            } else {
////                camera.cameraControl.enableTorch(false)
////                binding.flashToggleIB.setImageResource(R.drawable.flash_on)
////            }
////        } else {
////            Toast.makeText(
////                this.requireContext(),
////                "Flash is Not Available",
////                Toast.LENGTH_LONG
////            ).show()
////            binding.flashToggleIB.isEnabled = false
////        }
////    }
////
////    private fun takePhoto() {
////
////        val imageFolder = File(
////            Environment.getExternalStoragePublicDirectory(
////                Environment.DIRECTORY_PICTURES
////            ), "Images"
////        )
////        if (!imageFolder.exists()) {
////            imageFolder.mkdir()
////        }
////
////        val fileName = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
////            .format(System.currentTimeMillis()) + ".jpg"
////
////        val contentValues = ContentValues().apply {
////            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
////            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
////            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
////                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Images")
////            }
////        }
////
////        val metadata = ImageCapture.Metadata().apply {
////            isReversedHorizontal = (lensFacing == CameraSelector.LENS_FACING_FRONT)
////        }
////        val outputOption =
////            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
////                ImageCapture.OutputFileOptions.Builder(
////                    contentResolver,
////                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
////                    contentValues
////                ).setMetadata(metadata).build()
////            } else {
////                val imageFile = File(imageFolder, fileName)
////                ImageCapture.OutputFileOptions.Builder(imageFile)
////                    .setMetadata(metadata).build()
////            }
////
////        imageCapture.takePicture(
////            outputOption,
////            ContextCompat.getMainExecutor(this.requireContext()),
////            object : ImageCapture.OnImageSavedCallback {
////                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
////                    val message = "Photo Capture Succeeded: ${outputFileResults.savedUri}"
////                    Toast.makeText(
////                        this@CameraFragment.requireContext(),
////                        message,
////                        Toast.LENGTH_LONG
////                    ).show()
////                }
////
////                override fun onError(exception: ImageCaptureException) {
////                    Toast.makeText(
////                        this@CameraFragment.requireContext(),
////                        exception.message.toString(),
////                        Toast.LENGTH_LONG
////                    ).show()
////                }
////
////            }
////        )
////    }
////
////    private fun setAspectRatio(ratio: String) {
////        binding.previewView.layoutParams = binding.previewView.layoutParams.apply {
////            if (this is ConstraintLayout.LayoutParams) {
////                dimensionRatio = ratio
////            }
////        }
////    }
////
////    private fun captureVideo() {
////
////        binding.captureIB.isEnabled = false
////
////        binding.flashToggleIB.gone()
////        binding.flipCameraIB.gone()
////        binding.aspectRatioTxt.gone()
////        binding.changeCameraToVideoIB.gone()
////
////
////        val curRecording = recording
////        if (curRecording != null) {
////            curRecording.stop()
////            stopRecording()
////            recording = null
////            return
////        }
////        startRecording()
////        val fileName = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
////            .format(System.currentTimeMillis()) + ".mp4"
////
////        val contentValues = ContentValues().apply {
////            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
////            put(MediaStore.Images.Media.MIME_TYPE, "video/mp4")
////            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
////                put(MediaStore.Images.Media.RELATIVE_PATH, "Video")
////            }
////        }
////
////        val mediaStoreOutputOptions = MediaStoreOutputOptions
////            .Builder(contentResolver ,MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
////            .setContentValues(contentValues)
////            .build()
////
////        recording = videoCapture.output
////            .prepareRecording(this.requireContext(), mediaStoreOutputOptions)
////            .apply {
////                if (ActivityCompat.checkSelfPermission(
////                        this@CameraFragment.requireContext(),
////                        android.Manifest.permission.RECORD_AUDIO
////                    ) == PackageManager.PERMISSION_GRANTED
////                ) {
////                    withAudioEnabled()
////                }
////            }
////            .start(ContextCompat.getMainExecutor(this.requireContext())) { recordEvent ->
////                when (recordEvent) {
////                    is VideoRecordEvent.Start -> {
////                        binding.captureIB.setImageResource(R.drawable.ic_stop)
////                        binding.captureIB.isEnabled = true
////                    }
////
////                    is VideoRecordEvent.Finalize -> {
////                        if (!recordEvent.hasError()) {
////                            val message =
////                                "Video Capture Succeeded: " + "${recordEvent.outputResults.outputUri}"
////                            Toast.makeText(
////                                this@CameraFragment.requireContext(),
////                                message,
////                                Toast.LENGTH_LONG
////                            ).show()
////                        } else {
////                            recording?.close()
////                            recording = null
////                            Log.d("error", recordEvent.error.toString())
////                        }
////                        binding.captureIB.setImageResource(R.drawable.ic_start)
////                        binding.captureIB.isEnabled = true
////
////                        binding.flashToggleIB.visible()
////                        binding.flipCameraIB.visible()
////                        binding.aspectRatioTxt.visible()
////                        binding.changeCameraToVideoIB.visible()
////                    }
////                }
////            }
////
////    }
////
////
////    override fun onResume() {
////        super.onResume()
////        orientationEventListener?.enable()
////    }
////
////    override fun onPause() {
////        super.onPause()
////        orientationEventListener?.disable()
////        if (recording != null) {
////            recording?.stop()
////            captureVideo()
////        }
////    }
////
////    private val handler = Handler(Looper.getMainLooper())
////    private val updateTimer = object : Runnable {
////        override fun run() {
////            val currentTime = SystemClock.elapsedRealtime() - binding.recodingTimerC.base
////            val timeString = currentTime.toFormattedTime()
////            binding.recodingTimerC.text = timeString
////            handler.postDelayed(this, 1000)
////        }
////    }
////
////    private fun Long.toFormattedTime(): String {
////        val seconds = ((this / 1000) % 60).toInt()
////        val minutes = ((this / (1000 * 60)) % 60).toInt()
////        val hours = ((this / (1000 * 60 * 60)) % 24).toInt()
////
////        return if (hours > 0) {
////            String.format("%02d:%02d:%02d", hours, minutes, seconds)
////        } else {
////            String.format("%02d:%02d", minutes, seconds)
////        }
////    }
////
////    private fun startRecording() {
////        binding.recodingTimerC.visible()
////        binding.recodingTimerC.base = SystemClock.elapsedRealtime()
////        binding.recodingTimerC.start()
////        handler.post(updateTimer)
////    }
////
////    private fun stopRecording() {
////        binding.recodingTimerC.gone()
////        binding.recodingTimerC.stop()
////        handler.removeCallbacks(updateTimer)
////    }
//}
//
//
//
//
////    override fun onCreateView(
////        inflater: LayoutInflater,
////        container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        val application = requireNotNull(activity).application
//////val binding = Fr
//////        val binding = FragmentCameraBinding.inflate(inflater)
//////
//////        binding.lifecycleOwner = this
////
////        return super.onCreateView(inflater, container, savedInstanceState)
//////
////    }
//
////        binding = DataBindingUtil.inflate(
////            inflater, R.layout.fragment_camera, container, false
////        )
////
////        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
////        binding.viewModel = viewModel
////        binding.lifecycleOwner = this
////
////        return binding.root
//
////
////    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
////        super.onViewCreated(view, savedInstanceState)
////
////        if (ContextCompat.checkSelfPermission(
////                requireContext(),
////                Manifest.permission.CAMERA
////            ) != PackageManager.PERMISSION_GRANTED
////        ) {
////            requestPermissions(arrayOf(
////                Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
////        } else {
////            // Permission already granted
////            // Initialize camera here
////            initCamera()
////        }
////    }
////
////    private fun initCamera() {
////        // Open the camera
////        // Implement camera initialization logic here
////    }
////}
//
//
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
//////
//////    override fun onCreateView(
//////        inflater: LayoutInflater,
//////        container: ViewGroup?,
//////        savedInstanceState: Bundle?
//////    ): View? {
//////        val application = requireNotNull(activity).application
//////val binding = FragmentCameraBinding.inflate(inflater)
//////        binding.lifecycleOwner = this
////
//////        return binding.root //super.onCreateView(inflater, container, savedInstanceState)
////
//////    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//////        super.onViewCreated(view, savedInstanceState)
////
////
////        getPermissions()
////
////        textureView = textureView.findViewById(R.id.textureView)
////
////        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
////        handlerThread = HandlerThread("videoThread")
////        handlerThread.start()
////        handler = Handler((handlerThread).looper)
////
////        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
////            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
////                openCamera()
////            }
////
////            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
////            }
////
////            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
////                return false
////            }
////
////            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
////            }
////        }
////
////        imageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1)
////        imageReader.setOnImageAvailableListener(ImageReader.OnImageAvailableListener { reader ->
////            var image = reader?.acquireLatestImage()
////            var buffer = image!!.planes[0].buffer
////            var bytes = ByteArray(buffer.remaining())
////            buffer.get(bytes)
////
////            //                var file = File(getPermissions())
////            //                var opStream = FileOutputStream(file)
////            //                opStream.write(bytes)
////            //                opStream.close()
////
////            image.close()
////            //Toast.makeText("")
////            //                Toast.makeText(this@CameraFragment, "image captured", Toast.LENGTH_SHORT).show()
////            //            }
////            //        }, handler)
////            //            }
////            //    }
////
////
////            //        findViewById<Button>(R.id.capture).apply {
////            //            capture {
////
////            capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
////            capReq.addTarget(imageReader.surface)
////            cameraCaptureSession.capture(capReq.build(), null, null)
////        }
////    }
////
////    override fun onDestroy() {
////        super.onDestroy()
////        cameraDevice.close()
////        handler.removeCallbacksAndMessages(null)
////        handlerThread.quitSafely()
////    }
////
////    @SuppressLint("MissingPermission")
////    fun openCamera() {
////        cameraManager.openCamera(cameraManager.cameraIdList[0], object: CameraDevice.StateCallback() {
////
////            override fun onOpened(camera: CameraDevice) {
////                cameraDevice = camera
////
////                capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
////                var surface = Surface(textureView.surfaceTexture)
////                capReq.addTarget(surface)
////
////                cameraDevice.createCaptureSession(listOf(surface, imageReader.surface), object:
////                    CameraCaptureSession.StateCallback() {
////                    override fun onConfigured(session: CameraCaptureSession) {
////                        cameraCaptureSession = session
////                        cameraCaptureSession.setRepeatingRequest(capReq.build(), null, null)
////                    }
////
////                    override fun onConfigureFailed(session: CameraCaptureSession) {
////
////                    }
////
////                }, handler)
////            }
////            override fun onDisconnected(camera: CameraDevice) {
////            }
////
////            override fun onError(camera: CameraDevice, error: Int) {
////            }
////        }, handler)
////    }
////
////    fun getPermissions() {
////        var permissionList = mutableListOf<String>()
////
//////        if(checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
//////            permissionList.add(
//////                CAMERA
//////            )
//////        }
//////        if(checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(
//////            READ_EXTERNAL_STORAGE
//////        )
//////        if(checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(
//////            WRITE_EXTERNAL_STORAGE
//////        )
////
////            if(permissionList.size > 0) {
////                requestPermissions(permissionList.toTypedArray(), 101)
////
////            }
////    }
////
////    override fun onRequestPermissionsResult(
////        requestCode: Int,
////        permissions: Array<out String>,
////        grantResults: IntArray
////    ) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
////        grantResults.forEach {
////            if(it != PackageManager.PERMISSION_GRANTED) {
////                getPermissions()
////            }
////        }
//////        setContentView(R.layout.fragment_camera)
////
//
//
//
//
////    private lateinit var previewView: CameraFragment
////    private lateinit var cameraProvider: ProcessCameraProvider
////    private lateinit var cameraSelector: CameraSelector
////    private lateinit var preview: Preview
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(layout.fragment_camera)
////
////       //previewView = findViewById(id.preview)
////
////
////        // Initialize CameraX
////        if (allPermissionsGranted()) {
////            startCamera()
////        } else {
////            ActivityCompat.requestPermissions(
////                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
////            )
////        }
////    }
////
////    private fun allPermissionsGranted(): Boolean {
////        // Check for camera permissions
////        for (permission in REQUIRED_PERMISSIONS) {
////            if (ContextCompat.checkSelfPermission(
////                    this, permission
////                ) != PackageManager.PERMISSION_GRANTED
////            ) {
////                return false
////            }
////        }
////        return true
////    }
////
////    private fun startCamera() {
////        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
////
////        cameraProviderFuture.addListener({
////            // CameraProvider is now guaranteed to be available
////            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
////
////            // Set up the preview use case
////            val preview = Preview.Builder()
////                .build()
////                .also {
////                    it.setSurfaceProvider {
////                        preview
////                    }
////                }
////
////            // Select back camera
////            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
////
////            try {
////                // Unbind any existing use cases before rebinding
////                cameraProvider.unbindAll()
////
////                // Bind use cases to camera
////                cameraProvider.bindToLifecycle(
////                    this as LifecycleOwner, cameraSelector, preview
////                )
////
////            } catch (exc: Exception) {
////                Log.e(TAG, "Use case binding failed", exc)
////            }
////
////        }, ContextCompat.getMainExecutor(this))
////    }
////
////    // Handle permission request response
////    override fun onRequestPermissionsResult(
////        requestCode: Int, permissions: Array<String>, grantResults: IntArray
////    ) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
////        if (requestCode == REQUEST_CODE_PERMISSIONS) {
////            if (allPermissionsGranted()) {
////                startCamera()
////            } else {
////                Toast.makeText(
////                    this,
////                    "Permissions not granted by the user.",
////                    Toast.LENGTH_SHORT
////                ).show()
////                finish()
////            }
////        }
////    }
////
////    companion object {
////        private const val TAG = "YourCameraActivity"
////        private const val REQUEST_CODE_PERMISSIONS = 10
////        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
////    }
////}
