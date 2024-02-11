package com.example.groupproject.camera

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ExecutionException

class CameraViewModel: ViewModel() {
    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()


    val savedUri = MutableLiveData<Uri?>(null)

    var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA

    var screenAspectRatio = AspectRatio.RATIO_16_9

    var audioEnabled = MutableLiveData<Boolean>(true)

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
    override fun onCleared() {
        super.onCleared()
        savedUri.value = null
    }

    fun toggleSound() {
        audioEnabled.value = !audioEnabled.value!!
    }
    fun onCaptureClick(view: View) {
    }
    fun appSettingOpen(context: Context) {
        Toast.makeText(context, "Enable All Permissions", Toast.LENGTH_LONG).show()

        val settingIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        settingIntent.data = Uri.parse("package:${context.packageName}")

        context.startActivity(settingIntent)
    }

    fun warningPermissionDialog(context: Context, listener: DialogInterface.OnClickListener){
        MaterialAlertDialogBuilder(context)
            .setMessage("All Permission are Required for this app")
            .setCancelable(false)
            .setPositiveButton("Ok",listener)
            .create()
            .show()
    }

    fun View.visible(){
        visibility = View.VISIBLE
    }
    fun View.gone(){
        visibility = View.GONE
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

