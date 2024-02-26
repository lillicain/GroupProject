package com.example.groupproject.evilgarden

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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
    private val _isEditingPlantName = MutableLiveData<Boolean>(false)
    val isEditingPlantName: LiveData<Boolean> get() = _isEditingPlantName
    private val _isShowingPlantText = MutableLiveData<Boolean>(true)
    val isShowingPlantText: LiveData<Boolean> get() = _isShowingPlantText


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
                    PlantEnum.GLOOM_FRUIT -> R.drawable.gloom_fruit2
                    else -> R.drawable.eight_ball
                }
            }
        }

    }
    fun startWiggleAnimation(imageButton: ImageView) {
        val wiggle = ObjectAnimator.ofFloat(
            imageButton,
            "rotation",
            -5f,
            5f,
            -5f,
            5f,
            0f
        )
        wiggle.duration = 500 // Adjust the duration as needed
        wiggle.interpolator = AccelerateDecelerateInterpolator()
        wiggle.start()
    }
    fun swipeRight() {
        val currentIndex = user.value?.currentPlantIndex ?: 0

        if (_plants.value?.isNotEmpty() == true) {
            val newIndex = (currentIndex + 1) % (_plants.value?.size ?: 0)
            user.value?.currentPlantIndex = newIndex
            _currentPlant.value = _plants.value?.get(newIndex)
        }
        println(currentPlant.value?.name)
    }

    fun swipeLeft() {
        val currentIndex = user.value?.currentPlantIndex ?: 0

        if (_plants.value?.isNotEmpty() == true) {
            val newIndex = if (currentIndex > 0) currentIndex - 1 else _plants.value!!.lastIndex
            user.value?.currentPlantIndex = newIndex
            _currentPlant.value = _plants.value!![newIndex]
        }
    }

    fun water() {
        viewModelScope.launch {
            // Step 1: Retrieve the current plant from the database
            val currentPlant = _currentPlant.value

            // Check if the currentPlant is not null
            currentPlant?.let {
                // Check if user's XP is greater than or equal to 50
                if ((_user.value?.xp ?: 49) >= 50) {
                    _user.value?.xp = (_user.value?.xp ?: 0) - 50
                    // Step 2: Update the evilness value of the plant
                    it.evilness += 50

                    // Step 3: Save the updated plant back to the database
                    plantDatabase.updatePlant(it)
                    _user.value?.let { userDatabase.updateUser(it) }
                    // Step 4: Update the _currentPlant LiveData
                    _currentPlant.value = it.copy()
                }
            }
        }

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
    fun updatePlants() {
        viewModelScope.launch {
            _plants.value = plantDatabase.getAllItems()
        }

    }
    override fun onCleared() {
        super.onCleared()

        // Update user and plants in the Room database when the ViewModel is cleared
        viewModelScope.launch {
            _user.value?.let {
                userDatabase.insertUser(it)
            }
//
//            _plants.value?.let {
////                plantDatabase.insertPlantList(it)
//            }
        }
    }
    fun onEditPlantClicked() {
        startEditingPlantName()
    }
    fun onSavePlantNameClicked() {
        stopEditingPlantName()
    }
    fun startEditingPlantName() {
        _isEditingPlantName.value = true
        _isShowingPlantText.value = false
    }

    fun stopEditingPlantName() {
        _isEditingPlantName.value = false
        _isShowingPlantText.value = true
    }
}
