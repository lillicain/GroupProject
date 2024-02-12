package com.example.groupproject.camera

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel: ViewModel() {
    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

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

