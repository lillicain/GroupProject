package com.example.groupproject.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    val id: Int = 1,
    val username: String,
    var xp: Int
) {
}
enum class PlantEnum(val stringValue: String) {
    PIRANHA("Piranha"),
    EVIL_BUSH("Evil Bush"),
    GLOOM_FRUIT("Gloom Fruit"),
    DEMON_TREE("Demon Tree"),
    ANGRY_PUMPKIN("Angry Pumpkin"),
    DEATH_POTATO("Death Potato"),
    SIN_FLOWER("Sin Flower")


}
@Entity(tableName = "plant_table")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    val type: PlantEnum,
    var evilness: Int
)