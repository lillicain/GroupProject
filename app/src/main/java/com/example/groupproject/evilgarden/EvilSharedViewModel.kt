package com.example.groupproject.evilgarden

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupproject.database.EvilDatabase
import com.example.groupproject.database.UserDao
import kotlinx.coroutines.launch

// Shared ViewModel
class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val _userXP = MutableLiveData<Int>()
    val userXP: LiveData<Int> get() = _userXP

    private val userDao = EvilDatabase.getInstance(application).userDao()

    init {
        // Initialize _userXP from the database when the ViewModel is created
        viewModelScope.launch {
            _userXP.value = userDao.getUser()?.xp ?: 0
        }
    }

    fun updateUserXP(xpToAdd: Int) {
        // Update _userXP without immediately updating the database
        _userXP.value = (_userXP.value ?: 0) + xpToAdd
        // Optionally, update the Room database immediately if needed
        viewModelScope.launch {
            userDao.addXP(_userXP.value ?: 0)
        }
    }
}
