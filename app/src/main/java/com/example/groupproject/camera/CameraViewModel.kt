package com.example.groupproject.camera

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ExecutionException

class CameraViewModel: ViewModel() {
    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()
//    val processCameraProvider: LiveData<ProcessCameraProvider>
//        get() {
//            if (cameraProviderLiveData == null) {
//                cameraProviderLiveData = MutableLiveData()
//                val cameraProviderFuture =
//                    ProcessCameraProvider.getInstance(getApplication())
//                cameraProviderFuture.addListener(
//                    Runnable {
//                        try {
//                            cameraProviderLiveData!!.setValue(cameraProviderFuture.get())
//                        } catch (e: ExecutionException) {
//                            // Handle any errors (including cancellation) here.
//                            Log.e(TAG, "Unhandled exception", e)
//                        } catch (e: InterruptedException) {
//                            Log.e(TAG, "Unhandled exception", e)
//                        }
//                    },
//                    ContextCompat.getMainExecutor(getApplication())
//                )
//            }
//            return cameraProviderLiveData!!
//        }

    companion object {
        private const val TAG = "CameraViewModel"
    }
    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value += bitmap
    }

    fun onCaptureClick(view: View) {
    }


}

//@SuppressLint("RestrictedApi")
//class CameraActivity: ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//
//    }
//    companion object {
//        private val CAMERAX_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO)
//    }
//}

