package com.example.groupproject.evilgarden

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.widget.EditText
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
import java.util.UUID

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
            if (_user.value == null) {
                println("No Coke")
            } else {
                println("Has Coke")
            }
            println(_user.value?.username)
            if (_user.value != null && !_plants.value.isNullOrEmpty()) {
                val currentIndex = _user.value?.currentPlantIndex ?: 0
                if (currentIndex in _plants.value!!.indices) {
                    _currentPlant.value = _plants.value!![currentIndex]
                }
            } else if (_user.value == null ) { // THIS NEEDS TO CHECK OTHER STUFF ELSE IF IS NOT INCLUSIVE
                print("Eeffoc")
            } else if (_plants.value.isNullOrEmpty()) {
                plantDatabase.insertPlant(Plant(id = UUID.randomUUID(), "Malicious Bush", PlantEnum.EVIL_BUSH, 1))
                _plants.value = plantDatabase.getAllItems()
                val currentIndex = _user.value?.currentPlantIndex ?: 0
                if (currentIndex in _plants.value!!.indices) {
                    _currentPlant.value = _plants.value!![currentIndex]
                }

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
    fun showUserNameDialog(context: Context) {
        val editText = EditText(context)
        val dialog = AlertDialog.Builder(context)
            .setTitle("Enter Your Name")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                val userName = editText.text.toString()
                // Save the user with the entered name to the database
                viewModelScope.launch {
                userDatabase.insertUser(User(1, "$userName's Garden", 0, 0))
                _user.value = userDatabase.getUser()
                }
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Save the user with the entered name to the database
                viewModelScope.launch {
                userDatabase.insertUser(User(1, "Wonderful Garden", 0, 0))
                    _user.value = userDatabase.getUser()
                }
                // Handle cancel action if needed
            }
            .create()

        dialog.show()
    }
     fun updateUser() {
            viewModelScope.launch {
                _user.value = userDatabase.getUser()
            }

    }
}
