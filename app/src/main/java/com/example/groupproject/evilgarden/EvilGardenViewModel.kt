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

class EvilGardenViewModel(val database: EvilDatabase, application: Application) : ViewModel() {

    private var currentPlant = MutableLiveData<Plant?>()

    private val plants = database.plantDao().getAllItems()

    var plantImage: Int = R.drawable.evil_bush1
    // var room user
    // var room plants
    //
//    val allPlants: LiveData<List<Plant>>
    init {
        // Observe changes in currentPlant and update plantImage accordingly
        currentPlant.observeForever { plant ->
            plant?.let {
                // Check the type of plant and set the corresponding image resource
                plantImage = when (plant.type) {
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
//    var currentPlant: Plant = Plant()
    fun getRoomData() {

    }
    fun goToShop() {

    }
    fun water() {

    }
}