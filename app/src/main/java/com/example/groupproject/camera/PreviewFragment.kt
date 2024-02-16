//package com.example.groupproject.camera
//
//
//import ARG_MEDIA_PATH
//import ARG_PREVIEW_TYPE
//import android.content.pm.ActivityInfo
//import android.net.Uri
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import android.widget.MediaController
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import com.example.groupproject.databinding.FragmentCameraBinding
//import com.example.groupproject.databinding.FragmentPreviewBinding
//import com.example.groupproject.utils.MediaType
//
//class PreviewFragment : Fragment() {
//        private var mediaType: String? = null
//        private var mediaUri: Uri? = null
//
// lateinit var binding: FragmentPreviewBinding
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentPreviewBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            arguments?.let {
//                mediaType = it.getString(ARG_PREVIEW_TYPE)
//                mediaUri = Uri.parse(it.getString(ARG_MEDIA_PATH))
//            }
//            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
//
//        }
//
//        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//            super.onViewCreated(view, savedInstanceState)
//
//            when (mediaType) {
//                MediaType.IMAGE -> {
//                    binding.videoViewer.visibility = View.GONE
//                    binding.controllerView.visibility = View.GONE
//                    binding.ivPreview.visibility = View.VISIBLE
//                    binding.ivPreview.setImageURI(mediaUri)
//                }
//
//                MediaType.VIDEO -> {
//                    binding.videoViewer.visibility = View.VISIBLE
//                    binding.controllerView.visibility = View.VISIBLE
//                    binding.ivPreview.visibility = View.GONE
//                    val mc = MediaController(requireContext())
//                    val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
//                    params.bottomMargin = 200
//                    mc.layoutParams = params
//                    mc.setAnchorView(binding.controllerView)
//
//                    binding.videoViewer.apply {
//                        setVideoURI(mediaUri)
//                        setMediaController(mc)
//                        requestFocus()
//                    }.start()
//                }
//            }
//            binding.ivBack.setOnClickListener {
//                findNavController().popBackStack()
//            }
//        }
//        override fun onDestroyView() {
//            super.onDestroyView()
//            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
//        }
//    }
//
//////typealias CornersListener = () -> Unit
//////
//////class CameraXFragment : Fragment() {
//////
//////    private var preview: Preview? = null
//////    private var imageCapture: ImageCapture? = null
//////    private var imageAnalyzer: ImageAnalysis? = null
//////    private var camera: Camera? = null
//////
//////    private lateinit var safeContext: Context
//////
//////    private lateinit var outputDirectory: File
//////    private lateinit var cameraExecutor: ExecutorService
//////
//////    override fun onAttach(context: Context) {
//////        super.onAttach(context)
//////        safeContext = context
//////    }
//////
//////    private fun getStatusBarHeight(): Int {
//////        val resourceId =
//////            safeContext.resources.getIdentifier("status_bar_height", "dimen", "android")
//////        return if (resourceId > 0) {
//////            safeContext.resources.getDimensionPixelSize(resourceId)
//////        } else 0
//////    }
//////
//////    override fun onCreateView(
//////        inflater: LayoutInflater,
//////        container: ViewGroup?,
//////        savedInstanceState: Bundle?
//////    ): View? {
//////        // Inflate the layout for this fragment
//////        return inflater.inflate(R.layout.fragment_camera, container, false)
//////    }
//////
//////    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//////        super.onViewCreated(view, savedInstanceState)
//////
//////        // Request camera permissions
//////        if (allPermissionsGranted()) {
//////            startCamera()
//////        } else {
//////            ActivityCompat.requestPermissions(
//////                activity!!,
//////                REQUIRED_PERMISSIONS,
//////                REQUEST_CODE_PERMISSIONS
//////            )
//////        }
//////
//////        // Setup the listener for take photo button
////////        camera_capture_button.setOnClickListener { takePhoto() }
//////
//////        outputDirectory = getOutputDirectory()
//////
//////        cameraExecutor = Executors.newSingleThreadExecutor()
////////        cameraExecutor = Executors.newCachedThreadPool()
//////    }
//////
//////    private fun startCamera() {
////////        OpenCVLoader.initDebug()
//////        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)
//////
//////        cameraProviderFuture.addListener(Runnable {
//////            // Used to bind the lifecycle of cameras to the lifecycle owner
//////            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//////
//////            // Preview
//////            preview = Preview.Builder().build()
//////
//////            imageCapture = ImageCapture.Builder().build()
//////
//////            imageAnalyzer = ImageAnalysis.Builder().build().apply {
//////                setAnalyzer(Executors.newSingleThreadExecutor(), CornerAnalyzer {
////////                    val bitmap = viewFinder.bitmap
////////                    val img = Mat()
////////                    Utils.bitmapToMat(bitmap, img)
////////                    bitmap?.recycle()
//////                    // Do image analysis here if you need bitmap
//////                })
//////            }
//////            // Select back camera
//////            val cameraSelector =
//////                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
//////
//////            try {
//////                // Unbind use cases before rebinding
//////                cameraProvider.unbindAll()
//////                cameraProvider.bindToLifecycle(
//////                    this,
//////                    cameraSelector,
//////                    preview,
//////                    imageCapture,
//////                    imageAnalyzer
//////                )
//////                // Bind use cases to camera
////////                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalyzer, preview, imageCapture)
////////                preview?.setSurfaceProvider(viewFinder.createSurfaceProvider())
//////            } catch (exc: Exception) {
//////                Log.e(TAG, "Use case binding failed", exc)
//////            }
//////
//////        }, ContextCompat.getMainExecutor(safeContext))
//////
//////    }
//////}