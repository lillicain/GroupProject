package com.example.groupproject.evilgarden.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.groupproject.database.PlantDao
import com.example.groupproject.database.UserDao
import com.example.groupproject.evilgarden.EvilGardenViewModel

class EvilGardenShopViewModelFactory(
private val userDatabase: UserDao,
private val plantDatabase: PlantDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EvilGardenViewModel::class.java)) {
            return EvilGardenShopViewModel(userDatabase, plantDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
