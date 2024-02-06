package com.example.groupproject.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.groupproject.databinding.FragmentSearchBinding

class SearchFragment: Fragment() {
val viewModel: SearchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var editText = binding.searchText.text

        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner) {
            if (null != it) {

                viewModel.displayAllDetails()
            }
        }

//        binding.submitButton.setOnClickListener {
//            viewModel.userInput = editTextField.text.toString()
//
//                   }
//        val response = response
//        println(response.toString())
//        binding.responseText.text = response.toString()
          return binding.root

    }

}

