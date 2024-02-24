package com.example.groupproject.eightball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupproject.evilgarden.SharedViewModel

class EightBallViewModelFactory(private val sharedViewModel: SharedViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EightBallViewModel::class.java)) {
            return EightBallViewModel(
                sharedViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}