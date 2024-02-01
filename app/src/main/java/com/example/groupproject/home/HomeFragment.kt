package com.example.groupproject.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.groupproject.databinding.FragmentHomeBinding

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

        binding.magic8BallButton.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionToEightBall())
        }

        return binding.root
    }


}