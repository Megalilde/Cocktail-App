package com.example.mycocktail.data.remote.retrofit

import android.util.Log
import com.example.mycocktail.data.remote.api.DrinkApi
import com.example.mycocktail.data.remote.model.DrinkIdResponse
import com.example.mycocktail.data.remote.model.DrinkResponse
import com.example.mycocktail.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DrinkService {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val service: DrinkApi = retrofit.create(DrinkApi::class.java)

    suspend fun fetchDrinksFromApi(type: String): Result<DrinkResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getDrink(type)
                Result.success(response)
            } catch (e: Exception) {
                Log.e("Network error", "Errore nel prendere i dati", e)
                Result.failure(e)
            }
        }
    }



    suspend fun fetchDrinkByIdFromApi(id: String): Result<DrinkIdResponse?>{
        return withContext(Dispatchers.IO){
            try {
                val response = service.getDrinkDetailById(id)
                Result.success(response)
            }catch (e: Exception){
                Log.e("Network error", "Errore nel prendere i dati", e)
                Result.failure(e)
            }
        }

    }

    suspend fun fetchDrinkByNameFromApi(name: String): Result<DrinkIdResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getDrinkByName(name)
                Result.success(response)
            } catch (e: Exception) {
                Log.e("Network error", "Errore nel prendere i dati", e)
                Result.failure(e)
            }
        }
    }
}