package com.example.groupproject

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.groupproject.camera.CameraViewModel
import com.example.groupproject.cameraAwesome.DetectionFragment
import com.example.groupproject.databinding.ActivityCameraBinding
import com.example.groupproject.matthewcamera.CameraVM
import com.example.groupproject.utils.Permissions
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
//    private val viewModel: CameraViewModel by viewModels()
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.navigationBarColor = Color.parseColor("#80000000")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavController()
        binding.btnRequestPermission.setOnClickListener { requestPermissions() }
        if (!Permissions.isPermissionTaken(this)) {
            requestPermissions()
        }



    }
    fun moveToCamera() {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, DetectionFragment())
            .commit()
    }

    fun moveToPermission() {
        supportFragmentManager.beginTransaction()
//            .replace(android.R.id.content, PermissionsFragment())
            .commit()
    }

    private fun initNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
    private fun requestPermissions() {
        permissionLauncher.launch(Permissions.requestList)
    }
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val hasPermission = permissions.entries.all { it.value }
            binding.pnlPermission.visibility = if (!hasPermission) View.VISIBLE else View.GONE
        }
}



