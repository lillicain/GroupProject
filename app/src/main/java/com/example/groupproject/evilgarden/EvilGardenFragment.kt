package com.example.groupproject.evilgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import android.R
import com.example.groupproject.database.EvilDatabase
import com.example.groupproject.databinding.FragmentEvilGardenBinding

class EvilGardenFragment : Fragment() {

    private lateinit var binding: FragmentEvilGardenBinding
    private lateinit var viewModel: EvilGardenViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val viewModelFactory = EvilGardenViewModelFactory(EvilDatabase)
//        viewModel = ViewModelProvider(this,viewModelFactory).get(EvilGardenViewModel::class.java)
//        binding.lifecycleOwner = viewLifecycleOwner
        binding = FragmentEvilGardenBinding.inflate(inflater, container, false)
        if (isAdded) {
            try {
                // Attempt to get the EvilDatabase instance

                val evilDatabase = EvilDatabase.getInstance(requireContext())

                val viewModelFactory = EvilGardenViewModelFactory(evilDatabase)
                viewModel = ViewModelProvider(this, viewModelFactory).get(EvilGardenViewModel::class.java)
                binding.viewModel = viewModel
                // Continue with your logic using the evilDatabase instance
                // ...

            } catch (e: Exception) {
                // Handle any exceptions (e.g., logging, error reporting)
                e.printStackTrace()
            }
        }



        // Set up the Toolbar
//        binding.toolbar.title = "Evil Garden"
//        binding.toolbar.setNavigationIcon(R.drawable.btn_star) // Customize with your own icon
//        binding.toolbar.setNavigationOnClickListener {
//            findNavController().navigateUp() // Navigate back using the NavController
//        }
//        // Enable the Up button
//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        return binding.root
    }
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