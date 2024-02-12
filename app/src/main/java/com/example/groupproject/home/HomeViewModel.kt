package com.example.groupproject.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
//    private val _selectedProperty = MutableLiveData<NavigationName>()
//     val selectedProperty: LiveData<NavigationName>
//        get() = _selectedProperty
//
//    init {
//        _selectedProperty.value = NavigationName.homeFragment.toString()
//    }
}

enum class NavigationName {
    HOME_FRAGMENT,
    EIGHTBALL_FRAGMENT,
    MESSAGE_FRAGMENT,
    RINGTONE_FRAGMENT,
    SEARCH_FRAGMENT,
    CAMERA_FRAGMENT
}