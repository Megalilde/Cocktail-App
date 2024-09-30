package com.example.mycocktail.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "drinks-table")
data class Drink(
    val strDrink: String,
    val strDrinkThumb: String,
    @PrimaryKey
    val idDrink: String,
    var isFavorite: Boolean = false,
    var type: String
): Serializable
