package com.example.groupproject.evilgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.groupproject.databinding.FragmentEightBallBinding
import com.example.groupproject.databinding.FragmentEvilGardenBinding
import com.example.groupproject.databinding.FragmentHomeBinding
import com.example.groupproject.eightball.EightBallViewModel

class EvilGardenFragment : Fragment() {

    private lateinit var binding: FragmentEvilGardenBinding
    private val viewModel: EvilGardenViewModel by lazy {
        ViewModelProvider(this).get(EvilGardenViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentEvilGardenBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel  // Set up data binding
        binding.lifecycleOwner = viewLifecycleOwner // Set the lifecycle owner for LiveData
        return binding.root
       //TODO: create xml binding

    }
}