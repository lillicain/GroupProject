package com.example.groupproject.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SearchTextFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val binding = com.example.groupproject.databinding.FragmentSearchTextBinding.inflate(inflater)
//        binding.lifecycleOwner = this

//        val property
//        val viewModelFactory




        return binding.root
    }


}