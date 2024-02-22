package com.example.groupproject.camera


import ARG_MEDIA_PATH
import ARG_PREVIEW_TYPE
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.camera.view.PreviewView
import androidx.compose.ui.tooling.preview.Preview
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.groupproject.databinding.FragmentCameraBinding
import com.example.groupproject.databinding.FragmentPreviewBinding
import com.example.groupproject.utils.MediaType
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlin.math.max
import kotlin.math.min

class PreviewFragment : Fragment() {
        private var mediaType: String? = null
        private var mediaUri: Uri? = null

    private lateinit var inputImageView: ImageView
    private lateinit var imgSampleOne: ImageView
    private lateinit var imgSampleTwo: ImageView
    private lateinit var imgSampleThree: ImageView
    private lateinit var label: TextView
    private lateinit var currentPhotoPath: String
    lateinit var preview: PreviewView

 lateinit var binding: FragmentPreviewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                mediaType = it.getString(ARG_PREVIEW_TYPE)
                mediaUri = Uri.parse(it.getString(ARG_MEDIA_PATH))
            }
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

//            val bitmap = binding.overlay
            binding.ivPreview.setImageBitmap(preview.bitmap)
            binding.ivPreview.visibility = View.VISIBLE
//            cameraExecutor.shutdown()
            binding.ivPreview.visibility = View.GONE

            when (mediaType) {
                MediaType.IMAGE -> {
                    binding.videoViewer.visibility = View.VISIBLE
                    binding.controllerView.visibility = View.VISIBLE
                    binding.ivPreview.visibility = View.VISIBLE
                    binding.ivPreview.setImageURI(mediaUri)

                }

                MediaType.VIDEO -> {
                    binding.videoViewer.visibility = View.VISIBLE
                    binding.controllerView.visibility = View.VISIBLE
                    binding.ivPreview.visibility = View.GONE
                    val mc = MediaController(requireContext())
                    val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                    params.bottomMargin = 200
                    mc.layoutParams = params
                    mc.setAnchorView(binding.controllerView)

                    binding.videoViewer.apply {
                        setVideoURI(mediaUri)
                        setMediaController(mc)
                        requestFocus()

                    }.start()
                }
            }
            binding.ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        override fun onDestroyView() {
            super.onDestroyView()
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        }
    fun runObjectDetection(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)

        val options = ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build()
        val objectDetector = ObjectDetection.getClient(options)

        objectDetector.process(image).addOnSuccessListener { results ->
//            debugPrint(results)

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
//        binding.setImageBitmap(bitmap)
//        label.visibility = View.VISIBLE

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
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
                source, 0, 0, source.width, source.height,
                matrix, true
        )
    }

    /**
     * Get image form drawable and convert to bitmap.
     */
    private fun getSampleImage(drawable: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, drawable, BitmapFactory.Options().apply {
            inMutable = true
        })

    }
    private fun drawDetectionResult(
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
            pen.strokeWidth = 2F

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
