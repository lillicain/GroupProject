package com.example.groupproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("Select * FROM user_table WHERE id = 1")
    suspend fun getUser(): User?
    @Query("UPDATE user_table SET xp = xp + :xpToAdd WHERE id = 1")
    suspend fun addXP(xpToAdd: Int)
    @Query("UPDATE user_table SET xp = xp - :xpToSubtract WHERE id = 1")
    suspend fun subtractXP(xpToSubtract: Int)
    @Query("UPDATE user_table SET xp = :newXP WHERE id = 1")
    suspend fun setXP(newXP: Int)
    @Update
    suspend fun updateUser(user: User)
}
@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: Plant)

    @Query("SELECT * FROM plant_table WHERE id = :plantId")
    suspend fun getPlant(plantId: Long): Plant?

    @Query("SELECT * FROM plant_table")
    suspend fun getAllItems(): List<Plant>

    @Update
    suspend fun updatePlant(plant: Plant)
}