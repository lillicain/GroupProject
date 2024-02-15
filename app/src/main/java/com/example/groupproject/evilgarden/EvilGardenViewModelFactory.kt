package com.example.groupproject.evilgarden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupproject.database.EvilDatabase

class EvilGardenViewModelFactory(
    private val database: EvilDatabase
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EvilGardenViewModel::class.java)) {
            return EvilGardenViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
