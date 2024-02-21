package com.example.groupproject.evilgarden.shop

import androidx.lifecycle.ViewModel
import com.example.groupproject.database.EvilDatabase
import com.example.groupproject.database.PlantDao
import com.example.groupproject.database.UserDao

class EvilGardenShopViewModel(val userDatabase: UserDao, val plantDatabase: PlantDao): ViewModel() {
}