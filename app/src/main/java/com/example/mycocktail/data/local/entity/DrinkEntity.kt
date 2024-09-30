package com.example.mycocktail.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Classe di prova.

@Entity(tableName = "drinks-tabledd")
data class DrinkEntity (

    @ColumnInfo(name = "idDrink")
    @PrimaryKey
    val id: String = "",
    @ColumnInfo(name = "strDrink")
    val strDrink: String = "",
    @ColumnInfo(name = "url")
    val strDrinkThumb: String = "",
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean = false,

)