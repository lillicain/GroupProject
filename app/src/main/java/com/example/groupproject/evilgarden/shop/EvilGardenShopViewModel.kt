package com.example.groupproject.evilgarden.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupproject.database.EvilDatabase
import com.example.groupproject.database.Plant
import com.example.groupproject.database.PlantDao
import com.example.groupproject.database.PlantEnum
import com.example.groupproject.database.User
import com.example.groupproject.database.UserDao
import kotlinx.coroutines.launch
import java.util.UUID

class EvilGardenShopViewModel(val userDatabase: UserDao, val plantDatabase: PlantDao): ViewModel() {

    private var _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user
    init {
        viewModelScope.launch {
        _user.value = userDatabase.getUser()

        }
    }
    fun buyXP() {
        viewModelScope.launch {
        userDatabase.addXP(100)
        _user.value = userDatabase.getUser()
        }
    }
    fun buyPlant() {

    }
    fun buyGloomFruit() {
        viewModelScope.launch {
        plantDatabase.insertPlant(Plant(id = UUID.randomUUID(), "Gloomy Gloom Fruit", PlantEnum.GLOOM_FRUIT, 1))

        }
    }
}