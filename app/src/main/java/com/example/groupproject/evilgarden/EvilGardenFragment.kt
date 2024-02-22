package com.example.groupproject.evilgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import android.R
import android.app.AlertDialog
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.groupproject.database.EvilDatabase
import com.example.groupproject.database.User
import com.example.groupproject.databinding.FragmentEvilGardenBinding
import com.example.groupproject.home.HomeFragmentDirections

class EvilGardenFragment : Fragment() {

    private lateinit var binding: FragmentEvilGardenBinding
    private lateinit var viewModel: EvilGardenViewModel
    private lateinit var gestureDetector: GestureDetector


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



//        val viewModelFactory = EvilGardenViewModelFactory(EvilDatabase)
//        viewModel = ViewModelProvider(this,viewModelFactory).get(EvilGardenViewModel::class.java)
//        binding.lifecycleOwner = viewLifecycleOwner
        binding = FragmentEvilGardenBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val userDataSource = EvilDatabase.getInstance(application).userDao()
        val plantDataSource = EvilDatabase.getInstance(application).plantDao()
        val viewModelFactory = EvilGardenViewModelFactory(userDataSource, plantDataSource)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(EvilGardenViewModel::class.java)
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user == null) {
                // Show the user name dialog if there is no user data
                viewModel.showUserNameDialog(requireContext())
            } else {
//                println("EEFFOC")
//                viewModel.updateUser()
                // Update the UI with the existing user data
                // ...
            }
        })
        binding.viewModel = viewModel
        // Observe the user data in the ViewModel
        binding.lifecycleOwner = this

        binding.shopButton.setOnClickListener {
            this.findNavController().navigate(EvilGardenFragmentDirections.actionToEvilGardenShopFragment())

        }

        return binding.root
    }
    // Override the necessary methods for GestureDetector.OnGestureListener
    fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        val deltaX = e2?.x?.minus(e1?.x ?: 0f) ?: 0f
        val deltaY = e2?.y?.minus(e1?.y ?: 0f) ?: 0f

        // You can customize these thresholds based on your requirements
        val swipeThreshold = 150
        val swipeVelocityThreshold = 150

        if (kotlin.math.abs(deltaX) > swipeThreshold && kotlin.math.abs(velocityX) > swipeVelocityThreshold) {
            // Horizontal swipe detected
            if (deltaX > 0) {
                // Swipe right
                viewModel.swipeRight()
            } else {
                // Swipe left
                viewModel.swipeLeft()
            }
        }

        return true
    }

    override fun onResume() {
        super.onResume()
//        viewModel.updateUser()
//        viewModel.updatePlants()
    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            // Fragment is now visible
            updateDataIfNeeded()
        }
    }

    private fun updateDataIfNeeded() {
        viewModel.updateUser()
        viewModel.updatePlants()
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize ViewModel
//        viewModel = ViewModelProvider(this).get(EvilGardenViewModel::class.java)
//
//        // Check if there is a user in the database
//        if (viewModel.user.value == null) {
//            // Show dialog to get user name
//            viewModel.showUserNameDialog(requireContext())
//        }
//
//        // Rest of your onViewCreated logic
//        // ...
//    }

}
//class EvilGardenFragment : Fragment() {
//
//    private lateinit var binding: FragmentEvilGardenBinding
//    private val viewModel: EvilGardenViewModel by lazy {
//        ViewModelProvider(this).get(EvilGardenViewModel::class.java)
//    }
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
//        binding = FragmentEvilGardenBinding.inflate(inflater, container, false)
//        binding.viewModel = viewModel  // Set up data binding
//        binding.lifecycleOwner = viewLifecycleOwner // Set the lifecycle owner for LiveData
//        return binding.root
//       //TODO: create xml binding
//
//    }
//}

//        if (isAdded) {
//            try {
//                // Attempt to get the EvilDatabase instance
//                val application = requireNotNull(this.activity).application
//                val userDataSource = EvilDatabase.getInstance(application).userDao()
//                val plantDataSource = EvilDatabase.getInstance(application).plantDao()
//
//                val viewModelFactory = EvilGardenViewModelFactory(userDataSource, plantDataSource)
//                viewModel = ViewModelProvider(this, viewModelFactory).get(EvilGardenViewModel::class.java)
//                binding.viewModel = viewModel
//                // Continue with your logic using the evilDatabase instance
//                // ...
//
//            } catch (e: Exception) {
//                // Handle any exceptions (e.g., logging, error reporting)
//                e.printStackTrace()
//            }
//        }



// Set up the Toolbar
//        binding.toolbar.title = "Evil Garden"
//        binding.toolbar.setNavigationIcon(R.drawable.btn_star) // Customize with your own icon
//        binding.toolbar.setNavigationOnClickListener {
//            findNavController().navigateUp() // Navigate back using the NavController
//        }
//        // Enable the Up button
//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)