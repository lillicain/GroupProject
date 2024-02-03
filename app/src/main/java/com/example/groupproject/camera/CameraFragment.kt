package com.example.groupproject.camera

import android.Manifest.*
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.*
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.groupproject.DataBinderMapperImpl
import com.example.groupproject.R
import com.example.groupproject.R.*
import com.example.groupproject.R.id.*
import com.example.groupproject.databinding.FragmentSearchBinding
import com.example.groupproject.databinding.FragmentSearchBindingImpl
import java.io.File
import java.io.FileOutputStream

class CameraFragment: Fragment() {
//    lateinit var cameraManager: CameraManager
//    lateinit var textureView: TextureView
//    lateinit var cameraCaptureSession: CameraCaptureSession
//    lateinit var cameraDevice: CameraDevice
//    lateinit var captureRequest: CaptureRequest
//    lateinit var handler: Handler
//    lateinit var handlerThread: HandlerThread
//    lateinit var capReq: CaptureRequest.Builder
//    lateinit var imageReader: ImageReader
//    lateinit var viewModel: CameraViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
//val binding = Fr
//        val binding = FragmentCameraBinding.inflate(inflater)
//
//        binding.lifecycleOwner = this

        return super.onCreateView(inflater, container, savedInstanceState)
//
    }

    //        binding = DataBindingUtil.inflate(
//            inflater, R.layout.fragment_camera, container, false
//        )
//
//        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
//        binding.viewModel = viewModel
//        binding.lifecycleOwner = this
//
//        return binding.root

//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.CAMERA
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestPermissions(arrayOf(
//                Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
//        } else {
//            // Permission already granted
//            // Initialize camera here
//            initCamera()
//        }
//    }
//
//    private fun initCamera() {
//        // Open the camera
//        // Implement camera initialization logic here
//    }
//}


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////
////    override fun onCreateView(
////        inflater: LayoutInflater,
////        container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        val application = requireNotNull(activity).application
////val binding = FragmentCameraBinding.inflate(inflater)
////        binding.lifecycleOwner = this
//
////        return binding.root //super.onCreateView(inflater, container, savedInstanceState)
//
////    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
////        super.onViewCreated(view, savedInstanceState)
//
//
//        getPermissions()
//
//        textureView = textureView.findViewById(R.id.textureView)
//
//        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
//        handlerThread = HandlerThread("videoThread")
//        handlerThread.start()
//        handler = Handler((handlerThread).looper)
//
//        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
//            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
//                openCamera()
//            }
//
//            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
//            }
//
//            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
//                return false
//            }
//
//            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
//            }
//        }
//
//        imageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1)
//        imageReader.setOnImageAvailableListener(ImageReader.OnImageAvailableListener { reader ->
//            var image = reader?.acquireLatestImage()
//            var buffer = image!!.planes[0].buffer
//            var bytes = ByteArray(buffer.remaining())
//            buffer.get(bytes)
//
//            //                var file = File(getPermissions())
//            //                var opStream = FileOutputStream(file)
//            //                opStream.write(bytes)
//            //                opStream.close()
//
//            image.close()
//            //Toast.makeText("")
//            //                Toast.makeText(this@CameraFragment, "image captured", Toast.LENGTH_SHORT).show()
//            //            }
//            //        }, handler)
//            //            }
//            //    }
//
//
//            //        findViewById<Button>(R.id.capture).apply {
//            //            capture {
//
//            capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
//            capReq.addTarget(imageReader.surface)
//            cameraCaptureSession.capture(capReq.build(), null, null)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraDevice.close()
//        handler.removeCallbacksAndMessages(null)
//        handlerThread.quitSafely()
//    }
//
//    @SuppressLint("MissingPermission")
//    fun openCamera() {
//        cameraManager.openCamera(cameraManager.cameraIdList[0], object: CameraDevice.StateCallback() {
//
//            override fun onOpened(camera: CameraDevice) {
//                cameraDevice = camera
//
//                capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//                var surface = Surface(textureView.surfaceTexture)
//                capReq.addTarget(surface)
//
//                cameraDevice.createCaptureSession(listOf(surface, imageReader.surface), object:
//                    CameraCaptureSession.StateCallback() {
//                    override fun onConfigured(session: CameraCaptureSession) {
//                        cameraCaptureSession = session
//                        cameraCaptureSession.setRepeatingRequest(capReq.build(), null, null)
//                    }
//
//                    override fun onConfigureFailed(session: CameraCaptureSession) {
//
//                    }
//
//                }, handler)
//            }
//            override fun onDisconnected(camera: CameraDevice) {
//            }
//
//            override fun onError(camera: CameraDevice, error: Int) {
//            }
//        }, handler)
//    }
//
//    fun getPermissions() {
//        var permissionList = mutableListOf<String>()
//
////        if(checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
////            permissionList.add(
////                CAMERA
////            )
////        }
////        if(checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(
////            READ_EXTERNAL_STORAGE
////        )
////        if(checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(
////            WRITE_EXTERNAL_STORAGE
////        )
//
//            if(permissionList.size > 0) {
//                requestPermissions(permissionList.toTypedArray(), 101)
//
//            }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        grantResults.forEach {
//            if(it != PackageManager.PERMISSION_GRANTED) {
//                getPermissions()
//            }
//        }
////        setContentView(R.layout.fragment_camera)
//

}


//    private lateinit var previewView: CameraFragment
//    private lateinit var cameraProvider: ProcessCameraProvider
//    private lateinit var cameraSelector: CameraSelector
//    private lateinit var preview: Preview
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(layout.fragment_camera)
//
//       //previewView = findViewById(id.preview)
//
//
//        // Initialize CameraX
//        if (allPermissionsGranted()) {
//            startCamera()
//        } else {
//            ActivityCompat.requestPermissions(
//                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
//            )
//        }
//    }
//
//    private fun allPermissionsGranted(): Boolean {
//        // Check for camera permissions
//        for (permission in REQUIRED_PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(
//                    this, permission
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return false
//            }
//        }
//        return true
//    }
//
//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener({
//            // CameraProvider is now guaranteed to be available
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            // Set up the preview use case
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider {
//                        preview
//                    }
//                }
//
//            // Select back camera
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            try {
//                // Unbind any existing use cases before rebinding
//                cameraProvider.unbindAll()
//
//                // Bind use cases to camera
//                cameraProvider.bindToLifecycle(
//                    this as LifecycleOwner, cameraSelector, preview
//                )
//
//            } catch (exc: Exception) {
//                Log.e(TAG, "Use case binding failed", exc)
//            }
//
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//    // Handle permission request response
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<String>, grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (allPermissionsGranted()) {
//                startCamera()
//            } else {
//                Toast.makeText(
//                    this,
//                    "Permissions not granted by the user.",
//                    Toast.LENGTH_SHORT
//                ).show()
//                finish()
//            }
//        }
//    }
//
//    companion object {
//        private const val TAG = "YourCameraActivity"
//        private const val REQUEST_CODE_PERMISSIONS = 10
//        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
//    }
//
//}