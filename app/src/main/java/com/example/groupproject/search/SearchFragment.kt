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
val viewModel: SearchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
val searchUtil = SearchUtil()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       val binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.searchButtonFragment.setOnClickListener {
            searchUtil.userInputSearchData = binding.searchEditText.text.toString()
//            println(searchUtil.userInputSearchData.value?.text)
//            println(searchUtil.userInputSearchData.value?.text)
//            println(searchUtil.userInputSearchData.value?.text)
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // Call the suspend function
                    searchUtil.convertDataToClass()
                } catch (e: Exception) {
                    // Handle exceptions
                    println("Error: ${e.message}")
                }
            }
        }



//        var response = response
        return binding.root

    }

}

