package com.example.mycocktail.data.remote.api

import com.example.mycocktail.data.remote.model.DrinkIdResponse
import com.example.mycocktail.data.remote.model.DrinkResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


// https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic
// https://www.thecocktaildb.com/api/json/v1/1/
// https://www.thecocktaildb.com/api/json/v1/1/popular.php
// www.thecocktaildb.com/api/json/v1/1/filter.php?i=Dry_Vermouth,Gin,Anis

interface DrinkApi {
    @GET("filter.php")
    suspend fun getDrink(
        @Query("a") alcoholic: String
    ): DrinkResponse

    @GET("lookup.php")
    suspend fun getDrinkDetailById(
        @Query("i") id: String
    ): DrinkIdResponse

    @GET("search.php")
    suspend fun getDrinkByName(
        @Query("s") name: String
    ): DrinkIdResponse
}