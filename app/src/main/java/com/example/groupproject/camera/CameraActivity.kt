package com.example.groupproject.camera

import android.Manifest
import android.content.Intent
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraInfoUnavailableException
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.groupproject.R
import com.google.android.gms.common.annotation.KeepName
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.LocalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraActivity: AppCompatActivity() {
    private val requiredPermissions =
        mutableListOf(
            Manifest.permission.CAMERA
        ).toTypedArray()

    private var cameraxManager: CameraxManager? = null

    private lateinit var previewView: PreviewView
    private lateinit var focusRing: ImageView
    private lateinit var btnFlash: Button
    private lateinit var ivCapturePreview: ImageView
    private lateinit var btnCapturePhoto: Button
    private lateinit var btnChangeCameraType: Button
    private lateinit var btnStartCamera: Button
    private lateinit var btnStopCamera: Button
    private lateinit var btnStartReading: Button
    private lateinit var btnStopReading: Button
    private lateinit var tvReadResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        initViews()
//        checkCameraPermission()

        btnFlash.setOnClickListener {
            cameraxManager?.changeFlashStatus()

        }

        btnChangeCameraType.setOnClickListener {
            cameraxManager?.changeCameraType()
        }

        btnCapturePhoto.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                cameraxManager?.capturePhoto()
            }
        }

        btnStartCamera.setOnClickListener {
            cameraxManager?.startCamera()
        }

        btnStopCamera.setOnClickListener {
            cameraxManager?.destroyReferences()
        }

        btnStartReading.setOnClickListener {
            cameraxManager?.startReading()
        }

        btnStopReading.setOnClickListener {
            cameraxManager?.stopReading()
        }

    }

    private fun initViews() {
        previewView = findViewById(R.id.previewView)
        focusRing = findViewById(R.id.focusRing)
        btnFlash = findViewById(R.id.btnFlash)
        ivCapturePreview = findViewById(R.id.ivCapturePreview)
        btnCapturePhoto = findViewById(R.id.btnCapturePhoto)
        btnChangeCameraType = findViewById(R.id.btnChangeCameraType)
        btnStartCamera = findViewById(R.id.btnStartCamera)
        btnStopCamera = findViewById(R.id.btnStopCamera)
        btnStartReading = findViewById(R.id.btnStartReading)
        btnStopReading = findViewById(R.id.btnStopReading)
        tvReadResult = findViewById(R.id.tvReadResult)
    }

    private fun initCameraManager() {

        cameraxManager = CameraxManager.getInstance(
            this,
            null,
            previewView,
            focusRing,
            1
        )
        cameraxManager?.startCamera()

        cameraxManager?.setReaderFormats(
            ReaderType.FORMAT_QR_CODE.value,
            ReaderType.FORMAT_EAN_8.value,
            ReaderType.FORMAT_EAN_13.value,
            ReaderType.FORMAT_UPC_E.value,
            ReaderType.FORMAT_UPC_A.value,
            ReaderType.FORMAT_AZTEC.value
        )
        cameraxManager?.startReading()


        cameraxManager?.apply {
            setQrReadSuccessListener { result ->
                println("QR RESULT ----------> $result")
                tvReadResult.text = result
            }

            setFlashStatusChangedListener { status ->
                when (status) {
                    FlashStatus.ENABLED -> {
                        btnFlash.setBackgroundResource(R.drawable.baseline_flash_on_24)
                    }

                    FlashStatus.DISABLED -> {
                        btnFlash.setBackgroundResource(R.drawable.baseline_flash_off_24)
                    }
                }
            }

            setPhotoCaptureResultListener { capturedBitmap ->
                runOnUiThread {
                    ivCapturePreview.setImageBitmap(capturedBitmap)
                }
            }
        }
    }
}
///** Live preview demo app for ML Kit APIs using CameraX. */
//@KeepName
//@RequiresApi(VERSION_CODES.LOLLIPOP)
//class CameraXLivePreviewActivity :
//    AppCompatActivity(), OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
//
//    private var previewView: PreviewView? = null
//    private var graphicOverlay: GraphicOverlay? = null
//    private var cameraProvider: ProcessCameraProvider? = null
//    private var camera: Camera? = null
//    private var previewUseCase: Preview? = null
//    private var analysisUseCase: ImageAnalysis? = null
//    private var imageProcessor: VisionImageProcessor? = null
//    private var needUpdateGraphicOverlayImageSourceInfo = false
//    private var selectedModel = OBJECT_DETECTION
//    private var lensFacing = CameraSelector.LENS_FACING_BACK
//    private var cameraSelector: CameraSelector? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d(TAG, "onCreate")
//        if (savedInstanceState != null) {
//            selectedModel = savedInstanceState.getString(STATE_SELECTED_MODEL, OBJECT_DETECTION)
//        }
//        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
//        setContentView(R.layout.activity_vision_camerax_live_preview)
//        previewView = findViewById(R.id.preview_view)
//        if (previewView == null) {
//            Log.d(TAG, "previewView is null")
//        }
//        graphicOverlay = findViewById(R.id.graphic_overlay)
//        if (graphicOverlay == null) {
//            Log.d(TAG, "graphicOverlay is null")
//        }
//        val spinner = findViewById<Spinner>(R.id.spinner)
//        val options: MutableList<String> = ArrayList()
//        options.add(OBJECT_DETECTION)
//        options.add(OBJECT_DETECTION_CUSTOM)
//        options.add(CUSTOM_AUTOML_OBJECT_DETECTION)
//        options.add(FACE_DETECTION)
//        options.add(BARCODE_SCANNING)
//        options.add(IMAGE_LABELING)
//        options.add(IMAGE_LABELING_CUSTOM)
//        options.add(CUSTOM_AUTOML_LABELING)
//        options.add(POSE_DETECTION)
//        options.add(SELFIE_SEGMENTATION)
//        options.add(TEXT_RECOGNITION_LATIN)
//        options.add(TEXT_RECOGNITION_CHINESE)
//        options.add(TEXT_RECOGNITION_DEVANAGARI)
//        options.add(TEXT_RECOGNITION_JAPANESE)
//        options.add(TEXT_RECOGNITION_KOREAN)
//        options.add(FACE_MESH_DETECTION)
//
//        // Creating adapter for spinner
//        val dataAdapter = ArrayAdapter(this, R.layout.spinner_style, options)
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        // attaching data adapter to spinner
//        spinner.adapter = dataAdapter
//        spinner.onItemSelectedListener = this
//        val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
//        facingSwitch.setOnCheckedChangeListener(this)
//        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
//            .get(CameraXViewModel::class.java)
//            .processCameraProvider
//            .observe(
//                this,
//                Observer { provider: ProcessCameraProvider? ->
//                    cameraProvider = provider
//                    bindAllCameraUseCases()
//                }
//            )
//
//        val settingsButton = findViewById<ImageView>(R.id.settings_button)
//        settingsButton.setOnClickListener {
//            val intent = Intent(applicationContext, SettingsActivity::class.java)
//            intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.CAMERAX_LIVE_PREVIEW)
//            startActivity(intent)
//        }
//    }
//
//    override fun onSaveInstanceState(bundle: Bundle) {
//        super.onSaveInstanceState(bundle)
//        bundle.putString(STATE_SELECTED_MODEL, selectedModel)
//    }
//
//    @Synchronized
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
//        // An item was selected. You can retrieve the selected item using
//        // parent.getItemAtPosition(pos)
//        selectedModel = parent?.getItemAtPosition(pos).toString()
//        Log.d(TAG, "Selected model: $selectedModel")
//        bindAnalysisUseCase()
//    }
//
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//        // Do nothing.
//    }
//
//    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
//        if (cameraProvider == null) {
//            return
//        }
//        val newLensFacing =
//            if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
//                CameraSelector.LENS_FACING_BACK
//            } else {
//                CameraSelector.LENS_FACING_FRONT
//            }
//        val newCameraSelector = CameraSelector.Builder().requireLensFacing(newLensFacing).build()
//        try {
//            if (cameraProvider!!.hasCamera(newCameraSelector)) {
//                Log.d(TAG, "Set facing to " + newLensFacing)
//                lensFacing = newLensFacing
//                cameraSelector = newCameraSelector
//                bindAllCameraUseCases()
//                return
//            }
//        } catch (e: CameraInfoUnavailableException) {
//            // Falls through
//        }
//        Toast.makeText(
//            applicationContext,
//            "This device does not have lens with facing: $newLensFacing",
//            Toast.LENGTH_SHORT
//        )
//            .show()
//    }
//
//    public override fun onResume() {
//        super.onResume()
//        bindAllCameraUseCases()
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//        imageProcessor?.run { this.stop() }
//    }
//
//    public override fun onDestroy() {
//        super.onDestroy()
//        imageProcessor?.run { this.stop() }
//    }
//
//    private fun bindAllCameraUseCases() {
//        if (cameraProvider != null) {
//            // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
//            cameraProvider!!.unbindAll()
//            bindPreviewUseCase()
//            bindAnalysisUseCase()
//        }
//    }
//
//    private fun bindPreviewUseCase() {
//        if (!PreferenceUtils.isCameraLiveViewportEnabled(this)) {
//            return
//        }
//        if (cameraProvider == null) {
//            return
//        }
//        if (previewUseCase != null) {
//            cameraProvider!!.unbind(previewUseCase)
//        }
//
//        val builder = Preview.Builder()
//        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
//        if (targetResolution != null) {
//            builder.setTargetResolution(targetResolution)
//        }
//        previewUseCase = builder.build()
//        previewUseCase!!.setSurfaceProvider(previewView!!.getSurfaceProvider())
//        camera =
//            cameraProvider!!.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector!!, previewUseCase)
//    }
//
//    private fun bindAnalysisUseCase() {
//        if (cameraProvider == null) {
//            return
//        }
//        if (analysisUseCase != null) {
//            cameraProvider!!.unbind(analysisUseCase)
//        }
//        if (imageProcessor != null) {
//            imageProcessor!!.stop()
//        }
//        imageProcessor =
//            try {
//                when (selectedModel) {
//                    OBJECT_DETECTION -> {
//                        Log.i(TAG, "Using Object Detector Processor")
//                        val objectDetectorOptions = PreferenceUtils.getObjectDetectorOptionsForLivePreview(this)
//                        ObjectDetectorProcessor(this, objectDetectorOptions)
//                    }
//                    OBJECT_DETECTION_CUSTOM -> {
//                        Log.i(TAG, "Using Custom Object Detector (with object labeler) Processor")
//                        val localModel =
//                            LocalModel.Builder().setAssetFilePath("custom_models/object_labeler.tflite").build()
//                        val customObjectDetectorOptions =
//                            PreferenceUtils.getCustomObjectDetectorOptionsForLivePreview(this, localModel)
//                        ObjectDetectorProcessor(this, customObjectDetectorOptions)
//                    }
//                    CUSTOM_AUTOML_OBJECT_DETECTION -> {
//                        Log.i(TAG, "Using Custom AutoML Object Detector Processor")
//                        val customAutoMLODTLocalModel =
//                            LocalModel.Builder().setAssetManifestFilePath("automl/manifest.json").build()
//                        val customAutoMLODTOptions =
//                            PreferenceUtils.getCustomObjectDetectorOptionsForLivePreview(
//                                this,
//                                customAutoMLODTLocalModel
//                            )
//                        ObjectDetectorProcessor(this, customAutoMLODTOptions)
//                    }
//                    TEXT_RECOGNITION_LATIN -> {
//                        Log.i(TAG, "Using on-device Text recognition Processor for Latin")
//                        TextRecognitionProcessor(this, TextRecognizerOptions.Builder().build())
//                    }
//                    TEXT_RECOGNITION_CHINESE -> {
//                        Log.i(TAG, "Using on-device Text recognition Processor for Latin and Chinese")
//                        TextRecognitionProcessor(this, ChineseTextRecognizerOptions.Builder().build())
//                    }
//                    TEXT_RECOGNITION_DEVANAGARI -> {
//                        Log.i(TAG, "Using on-device Text recognition Processor for Latin and Devanagari")
//                        TextRecognitionProcessor(this, DevanagariTextRecognizerOptions.Builder().build())
//                    }
//                    TEXT_RECOGNITION_JAPANESE -> {
//                        Log.i(TAG, "Using on-device Text recognition Processor for Latin and Japanese")
//                        TextRecognitionProcessor(this, JapaneseTextRecognizerOptions.Builder().build())
//                    }
//                    TEXT_RECOGNITION_KOREAN -> {
//                        Log.i(TAG, "Using on-device Text recognition Processor for Latin and Korean")
//                        TextRecognitionProcessor(this, KoreanTextRecognizerOptions.Builder().build())
//                    }
//                    FACE_DETECTION -> {
//                        Log.i(TAG, "Using Face Detector Processor")
//                        val faceDetectorOptions = PreferenceUtils.getFaceDetectorOptions(this)
//                        FaceDetectorProcessor(this, faceDetectorOptions)
//                    }
//                    BARCODE_SCANNING -> {
//                        Log.i(TAG, "Using Barcode Detector Processor")
//                        var zoomCallback: ZoomCallback? = null
//                        if (PreferenceUtils.shouldEnableAutoZoom(this)) {
//                            zoomCallback = ZoomCallback { zoomLevel: Float ->
//                                Log.i(TAG, "Set zoom ratio $zoomLevel")
//                                val ignored = camera!!.cameraControl.setZoomRatio(zoomLevel)
//                                true
//                            }
//                        }
//                        BarcodeScannerProcessor(this, zoomCallback)
//                    }
//                    IMAGE_LABELING -> {
//                        Log.i(TAG, "Using Image Label Detector Processor")
//                        LabelDetectorProcessor(this, ImageLabelerOptions.DEFAULT_OPTIONS)
//                    }
//                    IMAGE_LABELING_CUSTOM -> {
//                        Log.i(TAG, "Using Custom Image Label (Birds) Detector Processor")
//                        val localClassifier =
//                            LocalModel.Builder().setAssetFilePath("custom_models/bird_classifier.tflite").build()
//                        val customImageLabelerOptions =
//                            CustomImageLabelerOptions.Builder(localClassifier).build()
//                        LabelDetectorProcessor(this, customImageLabelerOptions)
//                    }
//                    CUSTOM_AUTOML_LABELING -> {
//                        Log.i(TAG, "Using Custom AutoML Image Label Detector Processor")
//                        val customAutoMLLabelLocalModel =
//                            LocalModel.Builder().setAssetManifestFilePath("automl/manifest.json").build()
//                        val customAutoMLLabelOptions =
//                            CustomImageLabelerOptions.Builder(customAutoMLLabelLocalModel)
//                                .setConfidenceThreshold(0f)
//                                .build()
//                        LabelDetectorProcessor(this, customAutoMLLabelOptions)
//                    }
//                    POSE_DETECTION -> {
//                        val poseDetectorOptions = PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
//                        val shouldShowInFrameLikelihood =
//                            PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
//                        val visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this)
//                        val rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this)
//                        val runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this)
//                        PoseDetectorProcessor(
//                            this,
//                            poseDetectorOptions,
//                            shouldShowInFrameLikelihood,
//                            visualizeZ,
//                            rescaleZ,
//                            runClassification,
//                            /* isStreamMode = */ true
//                        )
//                    }
//                    SELFIE_SEGMENTATION -> SegmenterProcessor(this)
//                    FACE_MESH_DETECTION -> FaceMeshDetectorProcessor(this)
//                    else -> throw IllegalStateException("Invalid model name")
//                }
//            } catch (e: Exception) {
//                Log.e(TAG, "Can not create image processor: $selectedModel", e)
//                Toast.makeText(
//                    applicationContext,
//                    "Can not create image processor: " + e.localizedMessage,
//                    Toast.LENGTH_LONG
//                )
//                    .show()
//                return
//            }
//
//        val builder = ImageAnalysis.Builder()
//        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
//        if (targetResolution != null) {
//            builder.setTargetResolution(targetResolution)
//        }
//        analysisUseCase = builder.build()
//
//        needUpdateGraphicOverlayImageSourceInfo = true
//
//        analysisUseCase?.setAnalyzer(
//            // imageProcessor.processImageProxy will use another thread to run the detection underneath,
//            // thus we can just runs the analyzer itself on main thread.
//            ContextCompat.getMainExecutor(this),
//            ImageAnalysis.Analyzer { imageProxy: ImageProxy ->
//                if (needUpdateGraphicOverlayImageSourceInfo) {
//                    val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
//                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
//                    if (rotationDegrees == 0 || rotationDegrees == 180) {
//                        graphicOverlay!!.setImageSourceInfo(imageProxy.width, imageProxy.height, isImageFlipped)
//                    } else {
//                        graphicOverlay!!.setImageSourceInfo(imageProxy.height, imageProxy.width, isImageFlipped)
//                    }
//                    needUpdateGraphicOverlayImageSourceInfo = false
//                }
//                try {
//                    imageProcessor!!.processImageProxy(imageProxy, graphicOverlay)
//                } catch (e: MlKitException) {
//                    Log.e(TAG, "Failed to process image. Error: " + e.localizedMessage)
//                    Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
//                }
//            }
//        )
//        cameraProvider!!.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector!!, analysisUseCase)
//    }
//
//    companion object {
//        private const val TAG = "CameraXLivePreview"
//        private const val OBJECT_DETECTION = "Object Detection"
//        private const val OBJECT_DETECTION_CUSTOM = "Custom Object Detection"
//        private const val CUSTOM_AUTOML_OBJECT_DETECTION = "Custom AutoML Object Detection (Flower)"
//        private const val FACE_DETECTION = "Face Detection"
//        private const val TEXT_RECOGNITION_LATIN = "Text Recognition Latin"
//        private const val TEXT_RECOGNITION_CHINESE = "Text Recognition Chinese"
//        private const val TEXT_RECOGNITION_DEVANAGARI = "Text Recognition Devanagari"
//        private const val TEXT_RECOGNITION_JAPANESE = "Text Recognition Japanese"
//        private const val TEXT_RECOGNITION_KOREAN = "Text Recognition Korean"
//        private const val BARCODE_SCANNING = "Barcode Scanning"
//        private const val IMAGE_LABELING = "Image Labeling"
//        private const val IMAGE_LABELING_CUSTOM = "Custom Image Labeling (Birds)"
//        private const val CUSTOM_AUTOML_LABELING = "Custom AutoML Image Labeling (Flower)"
//        private const val POSE_DETECTION = "Pose Detection"
//        private const val SELFIE_SEGMENTATION = "Selfie Segmentation"
//        private const val FACE_MESH_DETECTION = "Face Mesh Detection (Beta)"
//
//        private const val STATE_SELECTED_MODEL = "selected_model"
//    }
//}
//
//import android.app.Application
//import android.hardware.camera2.CameraCaptureSession
//import android.hardware.camera2.CameraDevice
//import android.hardware.camera2.CameraManager
//import android.hardware.camera2.CaptureRequest
//import android.media.ImageReader
//import android.os.Build
//import android.os.Bundle
//import android.os.Handler
//import android.os.HandlerThread
//import android.util.Log
//import android.view.OrientationEventListener
//import android.view.TextureView
//import androidx.camera.camera2.Camera2Config
//import androidx.camera.core.AspectRatio
//import androidx.camera.core.Camera
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.CameraXConfig
//import androidx.camera.core.ImageCapture
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.video.Recorder
//import androidx.camera.video.Recording
//import androidx.camera.video.VideoCapture
//import com.example.groupproject.R
//import com.example.groupproject.databinding.ActivityMainBinding
//
//
////class MainApplication : Application(), CameraXConfig.Provider {
////    override fun getCameraXConfig(): CameraXConfig {
////        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
////            .setAvailableCamerasLimiter(CameraSelector.DEFAULT_BACK_CAMERA)
////            .build()
////    }
////}
////
////class CameraApplication : Application(), CameraXConfig.Provider {
////    override fun getCameraXConfig(): CameraXConfig {
////        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
////            .setMinimumLoggingLevel(Log.ERROR).build()
////    }
////}
////class CameraActivity {
////    lateinit var cameraManager: CameraManager
////    lateinit var textureView: TextureView
////    lateinit var cameraCaptureSession: CameraCaptureSession
////    lateinit var cameraDevice: CameraDevice
////    lateinit var captureRequest: CaptureRequest
////    lateinit var handler: Handler
////    lateinit var handlerThread: HandlerThread
////    lateinit var capReq: CaptureRequest.Builder
////    lateinit var imageReader: ImageReader
////    lateinit var videoCapture: VideoCapture<Recorder>
////    lateinit var imageCapture: ImageCapture
////    lateinit var cameraProvider: ProcessCameraProvider
////    lateinit var camera: Camera
////    lateinit var cameraSelector: CameraSelector
////    var orientationEventListener: OrientationEventListener? = null
////    var lensFacing = CameraSelector.LENS_FACING_BACK
////    var aspectRatio = AspectRatio.RATIO_16_9
////    var recording: Recording? = null
////    var isPhoto = true
////    val mainBinding: ActivityMainBinding by lazy {
////        ActivityMainBinding.inflate(layoutInflater)
////    }
////    val multiplePermissionId = 14
////    val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
////        arrayListOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
////    } else {
////        arrayListOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
////    }
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////
////        setContentView(R.layout.fragment_camera)
//
////        setContentView(mainBinding.root)
//
//
////        getPermissions()
//
////            textureView = textureView.findViewById(id.textureView)
////        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
////        handlerThread = HandlerThread("videoThread")
////        handlerThread.start()
////        handler = Handler((handlerThread).looper)
////
////        textureView.surfaceTextureListener = object: TextureView.SurfaceTextureListener {
////            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
////                open_camera()
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
////        imageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1)
////        imageReader.setOnImageAvailableListener(object: ImageReader.OnImageAvailableListener {
////            override fun onImageAvailable(reader: ImageReader?) {
////                var image = reader?.acquireLatestImage()
////                var buffer = image!!.planes[0].buffer
////                var bytes = ByteArray(buffer.remaining())
////                buffer.get(bytes)
////                var file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "img.jpeg")
////                var opStream = FileOutputStream(file)
////                opStream.write(bytes)
////                opStream.close()
////                image.close()
////
////
////
////                Toast.makeText(this@MainActivity, "image captured", Toast.LENGTH_SHORT).show()
////            }
////        }, handler)
////
//////        findViewById<Button>(capture).apply {
//////            setOnClickListener {
//////                capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
//////                capReq.addTarget(imageReader.surface)
//////                cameraCaptureSession.capture(capReq.build(), null, null)
//////            }
//////        }
////    }
////
////    @SuppressLint("MissingPermission")
////    fun open_camera() {
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
////    override fun onDestroy() {
////        super.onDestroy()
////        cameraDevice.close()
////        handler.removeCallbacksAndMessages(null)
////        handlerThread.quitSafely()
////    }
////
////    fun getPermissions() {
////        var permissionList = mutableListOf<String>()
////
////        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) permissionList.add(
////            android.Manifest.permission.CAMERA
////        )
////        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(
////            android.Manifest.permission.READ_EXTERNAL_STORAGE
////        )
////        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(
////            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
////        )
////
////        if (permissionList.size > 0) {
////            requestPermissions(permissionList.toTypedArray(), 101)
////
////        }
////    }
////
////    override fun onRequestPermissionsResult(
////        requestCode: Int,
////        permissions: Array<out String>,
////        grantResults: IntArray
////    ) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
////        grantResults.forEach {
////            if (it != PackageManager.PERMISSION_GRANTED) {
////                getPermissions()
////            }
////        }
//
////    lateinit var cameraManager: CameraManager
////    lateinit var textureView: TextureView
////    lateinit var cameraCaptureSession: CameraCaptureSession
////    lateinit var cameraDevice: CameraDevice
////    lateinit var captureRequest: CaptureRequest
////    lateinit var handler: Handler
////    lateinit var handlerThread: HandlerThread
////    lateinit var capReq: CaptureRequest.Builder
////    lateinit var imageReader: ImageReader
////
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_main)
////
////        getPermissions()
////        textureView = textureView.findViewById(R.id.textureView)
////        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
////        handlerThread = HandlerThread("videoThread")
////        handlerThread.start()
////        handler = Handler((handlerThread).looper)
////
////        textureView.surfaceTextureListener = object: TextureView.SurfaceTextureListener {
////            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
////                open_camera()
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
////
////        }
////    }
////    @SuppressLint("MissingPermission")
////    fun open_camera() {
////        cameraManager.openCamera(cameraManager.cameraIdList[0], object: CameraDevice.StateCallback(){
////            override fun onOpened(camera: CameraDevice) {
////                cameraDevice = camera
////                capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
////                var surface = Surface(textureView.surfaceTexture)
////                capReq.addTarget(surface)
////
////                cameraDevice.createCaptureSession(listOf(surface, imageReader.surface), object: CameraCaptureSession.StateCallback {
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
////
////
////            override fun onDisconnected(camera: CameraDevice) {
////
////            }
////
////            override fun onError(camera: CameraDevice, error: Int) {
////
////            }
////
////        })
////    }
////
////    fun getPermissions() {
////        var permissionList = mutableListOf<String>()
////
////        if(checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) permissionList.add(android.Manifest.permission.CAMERA)
////        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
////        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
////
////        if(permissionList.size > 0) {
////            requestPermissions(permissionList.toTypedArray(), 101)
////
////        }
////    }
////
////    override fun onRequestPermissionsResult(
////        requestCode: Int,
////        permissions: Array<out String>,
////        grantResults: IntArray
////    ) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
////        grantResults.forEach {
////            if (it != PackageManager.PERMISSION_GRANTED) {
////                getPermissions()
////            }
////        }
////    }
////}
//
////    override fun onCreateView(
////        inflater: LayoutInflater,
////        container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        val application = requireNotNull(activity).application
//////val binding = Fr
////        val binding = FragmentCameraBinding.inflate(inflater)
////
////        binding.lifecycleOwner = this
////
////        return super.onCreateView(inflater, container, savedInstanceState)
//////
////    }
////
////    //        binding = DataBindingUtil.inflate(
//////            inflater, R.layout.fragment_camera, container, false
//////        )
//////
//////        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
//////        binding.viewModel = viewModel
//////        binding.lifecycleOwner = this
//////
//////        return binding.root
////
//////
//////    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//////        super.onViewCreated(view, savedInstanceState)
//////
//////        if (ContextCompat.checkSelfPermission(
//////                requireContext(),
//////                Manifest.permission.CAMERA
//////            ) != PackageManager.PERMISSION_GRANTED
//////        ) {
//////            requestPermissions(arrayOf(
//////                Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
//////        } else {
//////            // Permission already granted
//////            // Initialize camera here
//////            initCamera()
//////        }
//////    }
//////
//////    private fun initCamera() {
//////        // Open the camera
//////        // Implement camera initialization logic here
//////    }
//////}
////
////
//////    override fun onCreate(savedInstanceState: Bundle?) {
//////        super.onCreate(savedInstanceState)
////////
////////    override fun onCreateView(
////////        inflater: LayoutInflater,
////////        container: ViewGroup?,
////////        savedInstanceState: Bundle?
////////    ): View? {
////////        val application = requireNotNull(activity).application
////////val binding = FragmentCameraBinding.inflate(inflater)
////////        binding.lifecycleOwner = this
//////
////////        return binding.root //super.onCreateView(inflater, container, savedInstanceState)
//////
////////    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
////////        super.onViewCreated(view, savedInstanceState)
//////
//////
//////        getPermissions()
//////
//////        textureView = textureView.findViewById(R.id.textureView)
//////
//////        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
//////        handlerThread = HandlerThread("videoThread")
//////        handlerThread.start()
//////        handler = Handler((handlerThread).looper)
//////
//////        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
//////            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
//////                openCamera()
//////            }
//////
//////            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
//////            }
//////
//////            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
//////                return false
//////            }
//////
//////            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
//////            }
//////        }
//////
//////        imageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1)
//////        imageReader.setOnImageAvailableListener(ImageReader.OnImageAvailableListener { reader ->
//////            var image = reader?.acquireLatestImage()
//////            var buffer = image!!.planes[0].buffer
//////            var bytes = ByteArray(buffer.remaining())
//////            buffer.get(bytes)
//////
//////            //                var file = File(getPermissions())
//////            //                var opStream = FileOutputStream(file)
//////            //                opStream.write(bytes)
//////            //                opStream.close()
//////
//////            image.close()
//////            //Toast.makeText("")
//////            //                Toast.makeText(this@CameraFragment, "image captured", Toast.LENGTH_SHORT).show()
//////            //            }
//////            //        }, handler)
//////            //            }
//////            //    }
//////
//////
//////            //        findViewById<Button>(R.id.capture).apply {
//////            //            capture {
//////
//////            capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
//////            capReq.addTarget(imageReader.surface)
//////            cameraCaptureSession.capture(capReq.build(), null, null)
//////        }
//////    }
//////
//////    override fun onDestroy() {
//////        super.onDestroy()
//////        cameraDevice.close()
//////        handler.removeCallbacksAndMessages(null)
//////        handlerThread.quitSafely()
//////    }
//////
//////    @SuppressLint("MissingPermission")
//////    fun openCamera() {
//////        cameraManager.openCamera(cameraManager.cameraIdList[0], object: CameraDevice.StateCallback() {
//////
//////            override fun onOpened(camera: CameraDevice) {
//////                cameraDevice = camera
//////
//////                capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//////                var surface = Surface(textureView.surfaceTexture)
//////                capReq.addTarget(surface)
//////
//////                cameraDevice.createCaptureSession(listOf(surface, imageReader.surface), object:
//////                    CameraCaptureSession.StateCallback() {
//////                    override fun onConfigured(session: CameraCaptureSession) {
//////                        cameraCaptureSession = session
//////                        cameraCaptureSession.setRepeatingRequest(capReq.build(), null, null)
//////                    }
//////
//////                    override fun onConfigureFailed(session: CameraCaptureSession) {
//////
//////                    }
//////
//////                }, handler)
//////            }
//////            override fun onDisconnected(camera: CameraDevice) {
//////            }
//////
//////            override fun onError(camera: CameraDevice, error: Int) {
//////            }
//////        }, handler)
//////    }
//////
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
//////
//////    override fun onRequestPermissionsResult(
//////        requestCode: Int,
//////        permissions: Array<out String>,
//////        grantResults: IntArray
//////    ) {
//////        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//////        grantResults.forEach {
//////            if(it != PackageManager.PERMISSION_GRANTED) {
//////                getPermissions()
//////            }
//////        }
////////        setContentView(R.layout.fragment_camera)
//////
////
////}
//
////}