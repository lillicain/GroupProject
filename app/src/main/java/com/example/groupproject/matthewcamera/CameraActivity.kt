// CameraActivity.kt
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.FileProvider
import com.example.groupproject.R
import com.example.groupproject.databinding.ActivityCameraBinding

import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            // Image capture success, handle the captured image
        } else {
            // Image capture failed or was canceled
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize CameraView
        binding.previewView.post {
            startCamera()
        }

        // Set up capture button
        binding.captureButton.setOnClickListener {
            captureImage()
        }
    }

    private fun startCamera() {
        val previewView: PreviewView = binding.previewView

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Create a CameraSelector for the default back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Create a Preview
            val preview = Preview.Builder().build()

            // Set up image capture use case
            imageCapture = ImageCapture.Builder().build()

            // Attach the Preview use case to the PreviewView
            preview.setSurfaceProvider(previewView.surfaceProvider)

            // Bind the lifecycle of CameraView to the lifecycle owner with CameraSelector
            cameraProvider.bindToLifecycle(this, cameraSelector, preview)

        }, mainExecutor)
    }

    private fun captureImage() {
        // Specify where to save the captured image
        val outputDirectory = getOutputDirectory()

        // Create a File to save the image
        val photoFile = File(
            outputDirectory,
            "captured_image_${System.currentTimeMillis()}.jpg"
        )

        // Create a Uri from the File
        val photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            photoFile
        )

        // Set up image capture use case
        val imageCapture = imageCapture ?: return
        val metadata = ImageCapture.Metadata().apply {
            // Add any metadata you need here
        }

        // Create image capture options
        val options = ImageCapture.OutputFileOptions.Builder(photoFile)
            .setMetadata(metadata)
            .build()

        // Take the picture
        takePicture.launch(null)
    }



    private fun getOutputDirectory(): File {
        // You can customize the directory here
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }
}