package com.example.groupproject.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val id: Int = 1,
    val username: String,
    var xp: Int,
    var currentPlantIndex: Int
) {
}
enum class PlantEnum(val stringValue: String) {
    PIRANHA("Piranha"),
    EVIL_BUSH("Evil Bush"),
    GLOOM_FRUIT("Gloom Fruit"),
    DEMON_TREE("Demon Tree"),
    ANGRY_PUMPKIN("Angry Pumpkin"),
    DEATH_POTATO("Death Potato"),
    SIN_FLOWER("Sin Flower"),
    HATER_GRASS("Victim Grass"),



}
@Entity(tableName = "plant_table")
data class Plant(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var name: String,
    val type: PlantEnum,
    var evilness: Int,
    var xp: Int = 0
)