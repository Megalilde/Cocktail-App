package com.example.mycocktail.data.remote.model

import com.example.mycocktail.data.datamodels.Drink
import java.io.Serializable

data class DrinkResponse (
    val drinks: List<Drink>
): Serializable