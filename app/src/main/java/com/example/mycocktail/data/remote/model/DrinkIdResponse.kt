package com.example.mycocktail.data.remote.model

import java.io.Serializable

data class DrinkIdResponse(
    val drinks: List<DrinkId>
): Serializable
