package com.example.groupproject.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupproject.evilgarden.SharedViewModel

class SearchUtilViewModelFactory(private val sharedViewModel: SharedViewModel) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchUtil::class.java)) {
            return SearchUtil(
                sharedViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}