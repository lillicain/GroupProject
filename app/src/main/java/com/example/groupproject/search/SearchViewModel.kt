package com.example.groupproject.search

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel : ViewModel() {

    }
//
//    private val _status = MutableLiveData<MyAPIStatus>()
//    val status: MutableLiveData<MyAPIStatus>
//        get() = _status
//
//    private val _properties = MutableLiveData<List<SearchText>>()
//
//    val properties: LiveData<List<SearchText>>
//        get() = _properties
//
//    private val _navigateToSelectedProperty = MutableLiveData<SearchText>()
//    val navigateToSelectedProperty: LiveData<SearchText>
//        get() = _navigateToSelectedProperty
//
//    private var viewModelJob = Job()
//    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
//
//    init {
//        getItemDetails()
//    }
//
//    fun getItemDetails() {
//        viewModelScope.launch {
//            _status.value = MyAPIStatus.LOADING
//            try {
//                _status.value = MyAPIStatus.DONE
//            } catch (e: Exception) {
//                print(e)
//                _status.value = MyAPIStatus.ERROR
//                _properties.value = ArrayList()
//            }
//        }
//    }
//    override fun onCleared() {
//        super.onCleared()
//        viewModelJob.cancel()
//    }
//
//    fun displayDetails(property: SearchText) {
//        _navigateToSelectedProperty.value = property
//    }
//
//    fun displayAllDetails() {
//        _navigateToSelectedProperty.value = null
//    }
//
//    fun updateFilter() {
//        getItemDetails()
//    }
//}