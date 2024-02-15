package com.example.groupproject.camera

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupproject.camera.VideoFragment.Companion.TAG
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.ExecutionException
import kotlin.math.max
import kotlin.math.min

class CameraViewModel: ViewModel() {
    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null
    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()
    val savedUri = MutableLiveData<Uri?>(null)
    var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
    var screenAspectRatio = AspectRatio.RATIO_16_9
    var audioEnabled = MutableLiveData<Boolean>(true)

//        val processCameraProvider: LiveData<ProcessCameraProvider>
//        get() {
//            if (cameraProviderLiveData == null) {
//                cameraProviderLiveData = MutableLiveData()
//                val cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication())
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
        const val TAG = "CameraViewModel"
    }

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value += bitmap
    }

    override fun onCleared() {
        super.onCleared()
        savedUri.value = null
    }

    fun toggleSound() {
        audioEnabled.value = !audioEnabled.value!!
    }
}