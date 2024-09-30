package com.example.mycocktail.ui.model

import com.example.mycocktail.data.datamodels.Drink

data class DrinkResponseLocal (
    val drinks: MutableList<Drink> = mutableListOf()
)