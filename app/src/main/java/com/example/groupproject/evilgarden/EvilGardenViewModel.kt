package com.example.groupproject.evilgarden

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupproject.R
import com.example.groupproject.database.EvilDatabase
import com.example.groupproject.database.Plant
import com.example.groupproject.database.PlantDao
import com.example.groupproject.database.PlantEnum
import com.example.groupproject.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EvilGardenViewModel(val database: EvilDatabase) : ViewModel() {


    private var _currentPlant = MutableLiveData<Plant?>()
    val currentPlant: LiveData<Plant?>
        get() = _currentPlant

    private var _user = MutableLiveData<User?>()

    val user: LiveData<User?>
        get() = _user
//    private var user: User? = database.userDao().getUser()
    private val plants = database.plantDao().getAllItems()

    private var _plantImage = MutableLiveData<Int>()

    val plantImage: LiveData<Int>
        get() = (_plantImage ?: R.drawable.eight_ball) as LiveData<Int>

    // var room user
    // var room plants
    //
//    val allPlants: LiveData<List<Plant>>

    init {

        CoroutineScope(Dispatchers.Main).launch {
            _user.value = database.userDao().getUser()
        }
        if (_user.value != null && plants.isNotEmpty()) {
            val currentIndex = _user.value?.currentPlantIndex ?: 0

            // Ensure that the index is within the bounds of the plants list
            if (currentIndex in plants.indices) {
                _currentPlant.value = plants[currentIndex]
            }
        }
        _plantImage.value = R.drawable.eight_ball
        // Observe changes in currentPlant and update plantImage accordingly
        currentPlant.observeForever { plant ->
            plant?.let {
                // Check the type of plant and set the corresponding image resource
                _plantImage.value = when (plant.type) {
                    PlantEnum.EVIL_BUSH -> R.drawable.evil_bush1
                    PlantEnum.DEMON_TREE -> R.drawable.demon_tree
                    // Add more cases for other plant types
                    else -> R.drawable.eight_ball // Default image for unknown types
                }

                // Notify observers that plantImage has changed
                // This will trigger any observers watching plantImage to update
                // This is important for any UI components bound to plantImage
                // (e.g., if you're using Data Binding in your layout)
//                notifyPlantImageChanged()

            }
        }
        fun swipeRight() {
            val currentIndex = user.value?.currentPlantIndex ?: 0

            // Check if there are plants in the list
            if (plants.isNotEmpty()) {
                // Increment the current index
                val newIndex = (currentIndex + 1) % plants.size
                user.value?.currentPlantIndex = newIndex
                _currentPlant.value = plants[newIndex]
            }
        }
        fun swipeLeft() {
            val currentIndex = user.value?.currentPlantIndex ?: 0

            // Check if there are plants in the list
            if (plants.isNotEmpty()) {
                // Decrement the current index
                val newIndex = if (currentIndex > 0) currentIndex - 1 else plants.lastIndex
                user.value?.currentPlantIndex = newIndex
                _currentPlant.value = plants[newIndex]
            }
        }
        //    var currentPlant: Plant = Plant()
        fun getRoomData() {

        }

        fun goToShop() {

        }

        fun water() {

        }
    }
}