package com.example.groupproject.camera

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException

//class QRScanner : AppCompatActivity() {
//    var back_image: ImageView? = null
//    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
//    var previewView: PreviewView? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setContentView(android.R.layout.activity_q_r_scanner)
////        back_image = findViewById<ImageView>(android.R.id.back_img)
////        previewView = findViewById<PreviewView>(android.R.id.viewFinder)
//        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//        Log.i("Camerax", "preview view set up")
////        back_image.setOnClickListener(View.OnClickListener {
////        ,;    val r = Intent(this@QRScanner, Dashbord::class.java)
////            startActivity(r)
////            finish()
////        })
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
//        cameraProviderFuture!!.addListener({
//            try {
//                val cameraProvider = cameraProviderFuture!!.get()
//                Log.i("Camerax", "The CameraProvider is working now in try")
//                bindPreview(cameraProvider)
//            } catch (e: ExecutionException) {
//                // No errors need to be handled for this Future.
//                // This should never be reached.
//                Log.i("Camerax", "The Camerax catch portion")
//                e.printStackTrace()
//            } catch (e: InterruptedException) {
//                Log.i("Camerax", "The Camerax catch portion")
//                e.printStackTrace()
//            }
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//    fun bindPreview(cameraProvider: ProcessCameraProvider) {
//        val preview = Preview.Builder()
//            .build()
//        val cameraSelector = CameraSelector.Builder()
//            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//            .build()
//        preview.setSurfaceProvider(previewView!!.surfaceProvider)
//        Log.i("Camerax", "bindPreview is working")
//        val camera: Camera =
//            cameraProvider.bindToLifecycle((this as LifecycleOwner), cameraSelector, preview)
//    }
//}