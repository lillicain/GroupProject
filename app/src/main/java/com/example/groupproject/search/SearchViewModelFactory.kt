package com.example.groupproject.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModelFactory(val property: SearchText, private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchTextViewModel::class.java)) {
                return SearchTextViewModel(property, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}