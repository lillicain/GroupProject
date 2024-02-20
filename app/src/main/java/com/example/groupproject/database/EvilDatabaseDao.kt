package com.example.groupproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("Select * FROM user_table WHERE id = 1")
    suspend fun getUser(): User?
}
@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: Plant)

    @Query("SELECT * FROM plant_table WHERE id = :plantId")
    suspend fun getPlant(plantId: Long): Plant?

    @Query("SELECT * FROM plant_table")
    fun getAllItems(): List<Plant>
}