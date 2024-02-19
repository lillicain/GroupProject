package com.example.groupproject.evilgarden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupproject.database.EvilDatabase
import com.example.groupproject.database.PlantDao
import com.example.groupproject.database.UserDao

class EvilGardenViewModelFactory(
    private val userDatabase: UserDao,
    private val plantDatabase: PlantDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EvilGardenViewModel::class.java)) {
            return EvilGardenViewModel(userDatabase, plantDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
