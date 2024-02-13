package com.example.groupproject.search

//import SearchViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.groupproject.databinding.FragmentSearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragment: Fragment() {
    val viewModel: SearchUtil by lazy { ViewModelProvider(this)[SearchUtil::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


//        binding.searchButtonFragment.setOnClickListener {
//            viewModel.userInputSearchData = binding.searchEditText.text.toString()
//            viewModel.convertDataToClass()
//            CoroutineScope(Dispatchers.Main).launch {
//                try {
//                    // Call the suspend function
//                    viewModel.convertDataToClass()
//                } catch (e: Exception) {
//                    // Handle exceptions
//                    println("Error: ${e.message}")
//                }
//            }
//        }



//        var response = response
        return binding.root

    }

}

