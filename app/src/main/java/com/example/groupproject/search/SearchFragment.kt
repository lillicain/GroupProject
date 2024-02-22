package com.example.groupproject.search

//import SearchViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.groupproject.database.EvilDatabase
import com.example.groupproject.databinding.FragmentSearchBinding
import com.example.groupproject.evilgarden.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragment: Fragment() {
//    val sharedViewModel: SharedViewModel by activityViewModels()
    val viewModel: SearchUtil by lazy { ViewModelProvider(this)[SearchUtil()::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.searchButtonFragment.setOnClickListener {
            viewModel.userInputSearchData = binding.searchEditText.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // Call the suspend function
                    viewModel.convertDataToClass()
                } catch (e: Exception) {
                    // Handle exceptions
                    println("Error: ${e.message}")
                }
            }
        }



//        var response = response
        return binding.root

    }

    override fun onPause() {
        super.onPause()
        val userDao = EvilDatabase.getInstance(requireContext()).userDao()
        lifecycleScope.launch {
//            sharedViewModel.saveUserToDatabase(userDao)
        }

    }

}

