package com.example.groupproject.evilgarden

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupproject.database.UserDao
import kotlinx.coroutines.launch

// Shared ViewModel
class SharedViewModel(userDao: UserDao) : ViewModel() {
    private val _userXP = MutableLiveData<Int>()
    val userXP: LiveData<Int> get() = _userXP

    init {
        // Initialize _userXP from the database when the ViewModel is created
        viewModelScope.launch {
            _userXP.value = userDao.getUser()?.xp ?: 0
        }
    }

    fun updateUserXP(xpToAdd: Int) {
        // Update _userXP without immediately updating the database
        _userXP.value = (_userXP.value ?: 0) + xpToAdd
    }

    suspend fun saveUserToDatabase(userDao: UserDao) {
        // Update the Room database with the latest XP
        userDao.addXP(_userXP.value ?: 0)
    }
}