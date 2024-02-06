package com.example.groupproject.ringtone

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RingtoneViewModel(ringtone: RingtoneProperty, app: Application): ViewModel() {
    private val _selectedRingtone = MutableLiveData<RingtoneProperty>()

    val selectedRingtone: LiveData<RingtoneProperty>
        get() = _selectedRingtone

    private val _ringtone_Properties = MutableLiveData<List<RingtoneProperty>>()
    val ringtone_Properties: LiveData<List<RingtoneProperty>>
        get() = _ringtone_Properties

    init {
        _selectedRingtone.value = ringtone
    }
}