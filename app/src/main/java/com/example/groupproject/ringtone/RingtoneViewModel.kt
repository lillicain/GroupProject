package com.example.groupproject.ringtone

import android.app.Application
import android.media.Ringtone
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RingtoneViewModel(ringtone: RingtoneData, app: Application) {
    private val _selectedRingtone = MutableLiveData<RingtoneData>()

    val selectedRingtone: LiveData<RingtoneData>
        get() = _selectedRingtone

    init {
        _selectedRingtone.value = ringtone
    }
}