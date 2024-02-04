package com.example.groupproject

import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.opengl.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.utils.MatrixExt.postRotate
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groupproject.R.*
import com.example.groupproject.camera.CameraViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0
            )

            setContent {

                val scope = rememberCoroutineScope()
                val scaffoldState = rememberBottomSheetScaffoldState()
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(
                            CameraController.IMAGE_CAPTURE or
                                    CameraController.VIDEO_CAPTURE
                        )
                    }
                }
                val viewModel = viewModel<CameraViewModel>()
                val bitmaps by viewModel.bitmaps.collectAsState()


//                BottomSheetScaffold(
//                    scaffoldState = scaffoldState,
//                    sheetPeekHeight = 0.dp,
//                    sheetContent = {
//                        PhotoBottomSheetContent(
//                            bitmaps = bitmaps,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                        )
//                    }
//                ) { padding ->
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(padding)
//                    ) {
//
//                        CameraPreview(
//                            controller = controller,
//                            modifier = Modifier
//                                .fillMaxSize()
//                        )
//
//                        IconButton(
//                            onClick = {
//                                controller.cameraSelector =
//                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
//                                        CameraSelector.DEFAULT_FRONT_CAMERA
//                                    } else CameraSelector.DEFAULT_BACK_CAMERA
//                            },
//                            modifier = Modifier
//                                .offset(16.dp, 16.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Cameraswitch,
//                                contentDescription = "Switch camera"
//                            )
//                        }

//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .align(Alignment.BottomCenter)
//                                .padding(16.dp),
//                            horizontalArrangement = Arrangement.SpaceAround
//                        ) {
//                            IconButton(
//                                onClick = {
//                                    scope.launch {
//                                        scaffoldState.bottomSheetState.expand()
//                                    }
//                                }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.Photo,
//                                    contentDescription = "Open gallery"
//                                )
//                            }
//                            IconButton(
//                                onClick = {
//                                    takePhoto(
//                                        controller = controller,
//                                        onPhotoTaken = viewModel::onTakePhoto
//                                    )
//                                }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.PhotoCamera,
//                                    contentDescription = "Take photo"
//                                )
//                            }
                        }
                    }
                }




    fun takePhoto(
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {

//                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        android.graphics.Matrix(),
                        true
                    )

                    onPhotoTaken(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }



    companion object {
         val CAMERAX_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
        )
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
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraDevice.close()
//        handler.removeCallbacksAndMessages(null)
//        handlerThread.quitSafely()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//setContentView(layout.activity_main)
//
//        getPermissions()
//
//        textureView = textureView.findViewById(id.textureView)
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
//        findViewById<Button>(id.capture).apply {
//            setOnClickListener {
//                capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
//                capReq.addTarget(imageReader.surface)
//                cameraCaptureSession.capture(capReq.build(), null, null)
//            }
//        }
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
//            if(it != PackageManager.PERMISSION_GRANTED) {
//                getPermissions()
//            }
//        }
//    }
//}

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
