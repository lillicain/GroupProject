package com.example.groupproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.view.OrientationEventListener
import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import com.example.groupproject.R.*
import com.example.groupproject.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var cameraCaptureSession: CameraCaptureSession
    lateinit var cameraDevice: CameraDevice
    lateinit var captureRequest: CaptureRequest
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread
    lateinit var capReq: CaptureRequest.Builder
    lateinit var imageReader: ImageReader


    val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val multiplePermissionId = 14
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        )
    } else {
        arrayListOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private lateinit var videoCapture: VideoCapture<Recorder>
    private var recording: Recording? = null

    private var isPhoto = true

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var cameraSelector: CameraSelector
    private var orientationEventListener: OrientationEventListener? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var aspectRatio = AspectRatio.RATIO_16_9

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_camera)


//        getPermissions()

//            textureView = textureView.findViewById(id.textureView)
//        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
//        handlerThread = HandlerThread("videoThread")
//        handlerThread.start()
//        handler = Handler((handlerThread).looper)
//
//        textureView.surfaceTextureListener = object: TextureView.SurfaceTextureListener {
//            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
//                open_camera()
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
//        imageReader.setOnImageAvailableListener(object: ImageReader.OnImageAvailableListener {
//            override fun onImageAvailable(reader: ImageReader?) {
//                var image = reader?.acquireLatestImage()
//                var buffer = image!!.planes[0].buffer
//                var bytes = ByteArray(buffer.remaining())
//                buffer.get(bytes)
//                var file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "img.jpeg")
//                var opStream = FileOutputStream(file)
//                opStream.write(bytes)
//                opStream.close()
//                image.close()
//
//
//
//                Toast.makeText(this@MainActivity, "image captured", Toast.LENGTH_SHORT).show()
//            }
//        }, handler)
//
////        findViewById<Button>(capture).apply {
////            setOnClickListener {
////                capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
////                capReq.addTarget(imageReader.surface)
////                cameraCaptureSession.capture(capReq.build(), null, null)
////            }
////        }
//    }
//
//    @SuppressLint("MissingPermission")
//    fun open_camera() {
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
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraDevice.close()
//        handler.removeCallbacksAndMessages(null)
//        handlerThread.quitSafely()
//    }
//
//    fun getPermissions() {
//        var permissionList = mutableListOf<String>()
//
//        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) permissionList.add(
//            android.Manifest.permission.CAMERA
//        )
//        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(
//            android.Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//
//        if (permissionList.size > 0) {
//            requestPermissions(permissionList.toTypedArray(), 101)
//
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        grantResults.forEach {
//            if (it != PackageManager.PERMISSION_GRANTED) {
//                getPermissions()
//            }
//        }

    }
}


//    lateinit var cameraManager: CameraManager
//    lateinit var textureView: TextureView
//    lateinit var cameraCaptureSession: CameraCaptureSession
//    lateinit var cameraDevice: CameraDevice
//    lateinit var captureRequest: CaptureRequest
//    lateinit var handler: Handler
//    lateinit var handlerThread: HandlerThread
//    lateinit var capReq: CaptureRequest.Builder
//    lateinit var imageReader: ImageReader
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        getPermissions()
//        textureView = textureView.findViewById(R.id.textureView)
//        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
//        handlerThread = HandlerThread("videoThread")
//        handlerThread.start()
//        handler = Handler((handlerThread).looper)
//
//        textureView.surfaceTextureListener = object: TextureView.SurfaceTextureListener {
//            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
//                open_camera()
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
//
//        }
//    }
//    @SuppressLint("MissingPermission")
//    fun open_camera() {
//        cameraManager.openCamera(cameraManager.cameraIdList[0], object: CameraDevice.StateCallback(){
//            override fun onOpened(camera: CameraDevice) {
//                cameraDevice = camera
//                capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//                var surface = Surface(textureView.surfaceTexture)
//                capReq.addTarget(surface)
//
//                cameraDevice.createCaptureSession(listOf(surface, imageReader.surface), object: CameraCaptureSession.StateCallback {
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
//
//
//            override fun onDisconnected(camera: CameraDevice) {
//
//            }
//
//            override fun onError(camera: CameraDevice, error: Int) {
//
//            }
//
//        })
//    }
//
//    fun getPermissions() {
//        var permissionList = mutableListOf<String>()
//
//        if(checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) permissionList.add(android.Manifest.permission.CAMERA)
//        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//        if(permissionList.size > 0) {
//            requestPermissions(permissionList.toTypedArray(), 101)
//
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        grantResults.forEach {
//            if (it != PackageManager.PERMISSION_GRANTED) {
//                getPermissions()
//            }
//        }
//    }
//}

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val application = requireNotNull(activity).application
////val binding = Fr
//        val binding = FragmentCameraBinding.inflate(inflater)
//
//        binding.lifecycleOwner = this
//
//        return super.onCreateView(inflater, container, savedInstanceState)
////
//    }
//
//    //        binding = DataBindingUtil.inflate(
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
//}
