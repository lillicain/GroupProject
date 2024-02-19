package com.example.groupproject.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map

class SearchTextViewModel(property: SearchText, app: Application) : AndroidViewModel(app)  {

    private val _selectedProperty = MutableLiveData<SearchText>()
    val selectedProperty: LiveData<SearchText>
        get() = _selectedProperty
    init {
        _selectedProperty.value = property
    }

    val displayPropertyType = selectedProperty.map {
        app.applicationContext
    }
}