package com.example.groupproject.ringtone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class RingtoneViewModel(): ViewModel() { // got rid of "app: Application"

    val ringtoneList = RingtoneManager.allMyRingtones

    private val _selectedRingtone = MutableLiveData<RingtoneProperty>()

    val selectedRingtone: LiveData<RingtoneProperty>
        get() = _selectedRingtone

    private val _ringtone_Properties = MutableLiveData<List<RingtoneProperty>>()
    val ringtone_Properties: LiveData<List<RingtoneProperty>>
        get() = _ringtone_Properties

    private val _navigateToSelectedRingtoneProperty = MutableLiveData<RingtoneProperty?>()
    val navigateToSelectedRingtoneProperty: LiveData<RingtoneProperty?>
        get() = _navigateToSelectedRingtoneProperty

    private val viewModeJob = Job()
    private val coroutineScope = CoroutineScope(viewModeJob + Dispatchers.Main)

    init {
        _ringtone_Properties.value = ringtoneList.toList()
    }

    private fun getRingtoneProperties() {
        viewModelScope.launch {
            try {
              _ringtone_Properties.value
            } catch (e: Exception) {
                _ringtone_Properties.value = ArrayList()
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModeJob.cancel()
    }

    fun displayRingtoneDetails(ringtoneProperty: RingtoneProperty) {
        _navigateToSelectedRingtoneProperty.value = ringtoneProperty
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedRingtoneProperty.value = null
    }
}