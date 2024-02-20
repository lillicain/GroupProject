package com.example.groupproject.home

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.groupproject.camera.CameraXLivePreviewActivity
import com.example.groupproject.cameraAwesome.ChooserActivity
import com.example.groupproject.databinding.FragmentHomeBinding
import com.example.groupproject.preference.CameraXSourceDemoPreferenceFragment

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

//
//        binding.photosGrid.adapter = GridAdapter(GridAdapter.OnClickListener {
//            viewModel.displayDictionaryWordDetails(it)
//        })
//
//        viewModel.navigateToSelectedWord.observe(viewLifecycleOwner) {
//            if (null != it) {
//                this.findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
//                viewModel.displayPropertyDetailsComplete()
//            }
//        }

        //TODO: Buttons navigation

        binding.magic8BallButton.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionToEightBall())
        }

        binding.cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
             startCamera()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
            }

//            this.findNavController().navigate(HomeFragmentDirections.actionToCameraFragment())

//            activity?.let { val intent = Intent (it, CameraXLivePreviewActivity::class.java)
//          it.startActivity(intent)
//            }
//            val intent = Intent(this, CameraXLivePreviewActivity::class.java)
//            startActivity(intent)
        }

        binding.ringtoneButton.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionToRingtoneFragment())
        }

        binding.cameraTestButton.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionToCameraTestFragment())

        }
        binding.evilGardenButton.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionToRingtoneFragment())
        }

        binding.searchButton.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionToSearchFragment())

        }
        return binding.root
    }

    fun startCamera() {
        val cameraIntent2 = Intent(requireContext(), ChooserActivity::class.java)
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivity(cameraIntent2)
        } catch (e: ActivityNotFoundException) {
            println(e)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
            // Denied
            }
        }

    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_CAMERA_PERMISSION = 1
    }
}


