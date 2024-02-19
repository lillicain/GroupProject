package com.example.groupproject.evilgarden

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupproject.R
import com.example.groupproject.database.EvilDatabase
import com.example.groupproject.database.Plant
import com.example.groupproject.database.PlantDao
import com.example.groupproject.database.PlantEnum
import com.example.groupproject.database.User
import com.example.groupproject.database.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EvilGardenViewModel(val userDatabase: UserDao, val plantDatabase: PlantDao) : ViewModel() {

    private var _currentPlant = MutableLiveData<Plant?>()
    val currentPlant: LiveData<Plant?>
    get() = _currentPlant

    private var _user = MutableLiveData<User?>()
    val user: LiveData<User?>
    get() = _user

    private val _plants = MutableLiveData<List<Plant>>()
    val plants: LiveData<List<Plant>>
    get() = _plants

    private var _plantImage = MutableLiveData<Int>()
    val plantImage: LiveData<Int>
    get() = (_plantImage ?: R.drawable.eight_ball) as LiveData<Int>

    init {
        viewModelScope.launch {
            _user.value = userDatabase.getUser()
            _plants.value = plantDatabase.getAllItems()

            if (_user.value != null && !_plants.value.isNullOrEmpty()) {
                val currentIndex = _user.value?.currentPlantIndex ?: 0
                if (currentIndex in _plants.value!!.indices) {
                    _currentPlant.value = _plants.value!![currentIndex]
                }
            } else if (_user.value == null ) {
                print("Eeffoc")
            }
        }

        _plantImage.value = R.drawable.eight_ball

        currentPlant.observeForever { plant ->
            plant?.let {
                _plantImage.value = when (plant.type) {
                    PlantEnum.EVIL_BUSH -> R.drawable.evil_bush1
                    PlantEnum.DEMON_TREE -> R.drawable.demon_tree
                    else -> R.drawable.eight_ball
                }
            }
        }
    }

    fun swipeRight() {
        val currentIndex = user.value?.currentPlantIndex ?: 0

        if (_plants.value?.isNotEmpty() == true) {
            val newIndex = (currentIndex + 1) % (_plants.value?.size ?: 0)
            user.value?.currentPlantIndex = newIndex
            _currentPlant.value = _plants.value?.get(newIndex)
        }
    }

    fun swipeLeft() {
        val currentIndex = user.value?.currentPlantIndex ?: 0

        if (_plants.value?.isNotEmpty() == true) {
            val newIndex = if (currentIndex > 0) currentIndex - 1 else _plants.value!!.lastIndex
            user.value?.currentPlantIndex = newIndex
            _currentPlant.value = _plants.value!![newIndex]
        }
    }

    fun getRoomData() {
        // Placeholder method
    }

    fun goToShop() {
        // Placeholder method
    }

    fun water() {
        // Placeholder method
    }
}
