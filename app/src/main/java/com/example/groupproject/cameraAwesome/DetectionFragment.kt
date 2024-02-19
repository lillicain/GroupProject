package com.example.groupproject.cameraAwesome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.groupproject.MainActivity
import com.example.groupproject.R
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executors

class DetectionFragment: Fragment() {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onResume() {
        super.onResume()
//        if (!PermissionsFragment.hasPermissions(requireContext())) {
        activity as? MainActivity?
//        }
    }
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
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder()
            .setTargetName("Preview")
            .build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = ObjectDetectionAnalyzer {
//            overlay?.post {
            updateOverlay(it)
//            }
        }

        imageAnalysis.setAnalyzer(
            Executors.newSingleThreadExecutor(),
            analyzer
        )

        try {
            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider {
                        preview
                    }
                }
//            preview.setSurfaceProvider(previewView.surfaceProvider)

        } catch (e: Exception) {
        }
    }

    private fun updateOverlay(detectedObjects: DetectedObjects) {

        if (R.id.overlay == null) {
            return
        }

        if (detectedObjects.objects.isEmpty()) {
//            overlay.set(emptyList())
            return
        }

//        overlay.setSize(detectedObjects.imageWidth, detectedObjects.imageHeight)

        val list = mutableListOf<BoxData>()

        for (obj in detectedObjects.objects) {

            val box = obj.boundingBox

            val label = obj.labels.joinToString { label ->
                val confidence: Int = label.confidence.times(100).toInt()
                "${label.text} $confidence%"
            }

            val text = if (label.isNotEmpty()) label else "unknown"

            list.add(BoxData(text, box))
        }

//        overlay.set(list)
    }

}