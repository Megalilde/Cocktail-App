package com.example.mycocktail.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mycocktail.data.datamodels.Drink

@Dao
interface DrinkDao {
    /*@Insert
    suspend fun insert(drink: Drink)

    @Query("DELETE FROM `drinks-table` WHERE idDrink = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM `drinks-table`")
    suspend fun deleteAll()

    @Query("SELECT * FROM `drinks-table`")
    suspend fun getDrinksFavorite(): MutableList<Drink>?*/


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drink: Drink)

    @Query("UPDATE `drinks-table` SET isFavorite = :isFavorite  WHERE idDrink = :id")
    suspend fun updateDrinkById(id: String, isFavorite: Boolean)

    @Query("SELECT * FROM `drinks-table` WHERE type = :type")
    suspend fun getDrinksByType(type: String): MutableList<Drink>?

    @Query("SELECT * FROM `drinks-table` WHERE isFavorite = 1")
    suspend fun getFavoriteDrinks(): MutableList<Drink>?

}