package com.example.groupproject.evilgarden.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.groupproject.database.EvilDatabase
import com.example.groupproject.databinding.FragmentEvilGardenBinding
import com.example.groupproject.databinding.FragmentEvilGardenShopBinding
import com.example.groupproject.evilgarden.EvilGardenViewModelFactory

class EvilGardenShopFragment: Fragment() {
    private lateinit var binding: FragmentEvilGardenShopBinding
    private lateinit var viewModel: EvilGardenShopViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentEvilGardenShopBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val userDataSource = EvilDatabase.getInstance(application).userDao()
        val plantDataSource = EvilDatabase.getInstance(application).plantDao()
        val viewModelFactory = EvilGardenViewModelFactory(userDataSource, plantDataSource)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(EvilGardenShopViewModel::class.java)

    }
}