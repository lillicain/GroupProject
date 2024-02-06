// CameraActivity.kt
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        // Set up capture button
        binding.captureButton.setOnClickListener {
            captureImage()
        }

        // Initialize CameraView
        binding.previewView.post {
            Log.d("Cam", "Camera initialization start")
            startCamera()
            Log.d("Cam", "Camera initialization end")
        }
    }
    private val CAMERA_PERMISSION_REQUEST_CODE = 100

// ...

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Camera permission not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Camera permission already granted, proceed with camera initialization
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, proceed with camera initialization
                startCamera()
            } else {
                // Camera permission denied, handle accordingly (e.g., show a message)
                Log.e("Cam", "Camera permission denied")
            }
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
        takePicture.launch(photoUri)
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