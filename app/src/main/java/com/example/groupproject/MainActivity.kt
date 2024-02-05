package com.example.groupproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.example.groupproject.camera.CameraFragment

class MainActivity : AppCompatActivity() {
    private lateinit var cameraFragment : CameraFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_take_photo.setOnClickListener { takeNewPhoto() }
        hasPermissionAndOpenCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityCameraFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this, "Camera success!", Toast.LENGTH_SHORT).show()
    }

    /**
     * Check if you have Camera Permission
     *      if you don't have it then permission is requested
     */
    private fun hasPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startActivityCameraFragment()
        } else {
            requestPermission()
        }
    }

    /**
     * Call the fragment that has the component of CameraFragment on layout
     */
    private fun startActivityCameraFragment() {
        cameraFragment = CameraFragment.newInstance(Configuration.Builder().build())
        supportFragmentManager.beginTransaction().replace(R.id.content, cameraFragment, "Nothing")
            .commit()
    }

    /**
     * Requesting permission to open the Camera and show Dialog on screen
     */
    private fun requestPermission() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        ActivityCompat.requestPermissions(this, permissions, PackageManager.PERMISSION_GRANTED)
    }

    private fun takeNewPhoto() {
        cameraFragment.takePhotoOrCaptureVideo(callBackListener, "/storage/self/primary", "photo_test0")
    }

    private var callBackListener: CameraFragmentResultListener = object: CameraFragmentResultListener {

        override fun onVideoRecorded(filePath: String?) {
            println(filePath)
        }

        override fun onPhotoTaken(bytes: ByteArray?, filePath: String?) {
            val intent = PreviewActivity.newIntentPhoto(this@MainActivity, filePath)
            startActivityForResult(intent, 200)
        }
    }
}