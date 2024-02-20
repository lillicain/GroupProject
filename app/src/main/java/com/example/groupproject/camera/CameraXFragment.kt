//package com.example.groupproject.camera
//
//import android.content.Context
//import android.graphics.Camera
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.example.groupproject.R
//import com.example.groupproject.camera.CameraViewModel.Companion.TAG
//import com.google.androidbrowserhelper.trusted.Utils
//import java.io.File
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//
//typealias CornersListener = () -> Unit
//
//class CameraXFragment : Fragment() {
//
//    private var preview: Preview? = null
//    private var imageCapture: ImageCapture? = null
//    private var imageAnalyzer: ImageAnalysis? = null
//    private var camera: Camera? = null
//
//  lateinit var safeContext: Context
////  lateinit var binding: FragmentCameraXBinding
//
//    private lateinit var outputDirectory: File
//    private lateinit var cameraExecutor: ExecutorService
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        safeContext = context
//    }
//
//    private fun getStatusBarHeight(): Int {
//        val resourceId =
//            safeContext.resources.getIdentifier("status_bar_height", "dimen", "android")
//        return if (resourceId > 0) {
//            safeContext.resources.getDimensionPixelSize(resourceId)
//        } else 0
//    }
//
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_camera, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Request camera permissions
////        if (allPermissionsGranted()) {
////            startCamera()
////        } else {
////            ActivityCompat.requestPermissions(
////                activity!!,
////                REQUIRED_PERMISSIONS,
////                REQUEST_CODE_PERMISSIONS
////            )
////        }
//
//        // Setup the listener for take photo button
////        camera_capture_button.setOnClickListener { takePhoto() }
//
////        outputDirectory = getOutputDirectory()
//
//        cameraExecutor = Executors.newSingleThreadExecutor()
////        cameraExecutor = Executors.newCachedThreadPool()
//    }
//
//    private fun startCamera() {
////        OpenCVLoader.initDebug()
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)
//
//        cameraProviderFuture.addListener(Runnable {
//            // Used to bind the lifecycle of cameras to the lifecycle owner
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            // Preview
//            preview = Preview.Builder().build()
//
//            imageCapture = ImageCapture.Builder().build()
//
//imageAnalyzer = ImageAnalysis.Builder().build().apply {
//    setAnalyzer(Executors.newSingleThreadExecutor(), CornerAnalyzer {
//                    val bitmap = viewFinder.bitmap
////                    val img = Mat()
//                    Utils.bitmapToMat(bitmap, img)
//                    bitmap?.recycle()
//        // Do image analysis here if you need bitmap
//    })
//}
//            // Select back camera
//            val cameraSelector =
//                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
//
//            try {
//                // Unbind use cases before rebinding
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    this,
//                    cameraSelector,
//                    preview,
//                    imageCapture,
//                    imageAnalyzer
//                )
//                // Bind use cases to camera
//                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalyzer, preview, imageCapture)
//                preview?.setSurfaceProvider(viewFinder.createSurfaceProvider())
//            } catch (exc: Exception) {
//                Log.e(TAG, "Use case binding failed", exc)
//            }
//
//        }, ContextCompat.getMainExecutor(safeContext))
//
//    }
//}