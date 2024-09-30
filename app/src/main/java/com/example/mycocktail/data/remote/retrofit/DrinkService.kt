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
                val response = service.getDrink(type).execute()
                if (response.isSuccessful) {
                    Result.success(response.body())
                } else {
                    when (response.code()) {
                        400 -> Log.e("Error 400", "Bad Request - Check your request parameters.")
                        401 -> Log.e("Error 401", "Unauthorized - Check your API key.")
                        403 -> Log.e("Error 403", "Forbidden - You might not have access.")
                        404 -> Log.e("Error 404", "Not Found - The endpoint might be wrong.")
                        500 -> Log.e("Error 500", "Internal Server Error - Problem with the server.")
                        else -> Log.e("Error", "Unexpected Error: ${response.code()}")
                    }
                    Result.failure(Exception("HTTP error with code ${response.code()}"))

                }
            } catch (e: Exception) {
                Log.e("Network error", "Errore nel prendere i dati", e)
                Result.failure(e)
            }
        }
    }



    suspend fun fetchDrinkByIdFromApi(id: String): Result<DrinkIdResponse?>{
        return withContext(Dispatchers.IO){
            try {
                val response = service.getDrinkDetailById(id).execute()
                if (response.isSuccessful) {
                    Result.success(response.body())
                }else{
                    when (response.code()) {
                        400 -> Log.e("Error 400", "Bad Request - Check your request parameters.")
                        401 -> Log.e("Error 401", "Unauthorized - Check your API key.")
                        403 -> Log.e("Error 403", "Forbidden - You might not have access.")
                        404 -> Log.e("Error 404", "Not Found - The endpoint might be wrong.")
                        500 -> Log.e("Error 500", "Internal Server Error - Problem with the server.")
                        else -> Log.e("Error", "Unexpected Error: ${response.code()}")
                    }
                    Result.failure(Exception("HTTP error with code ${response.code()}"))
                }
            }catch (e: Exception){
                Log.e("Network error", "Errore nel prendere i dati", e)
                Result.failure(e)
            }
        }

    }

    suspend fun fetchDrinkByNameFromApi(name: String): Result<DrinkIdResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getDrinkByName(name).execute()
                if (response.isSuccessful) {
                    Result.success(response.body())
                } else {
                    when (response.code()) {
                        400 -> Log.e("Error 400", "Bad Request - Check your request parameters.")
                        401 -> Log.e("Error 401", "Unauthorized - Check your API key.")
                        403 -> Log.e("Error 403", "Forbidden - You might not have access.")
                        404 -> Log.e("Error 404", "Not Found - The endpoint might be wrong.")
                        500 -> Log.e(
                            "Error 500",
                            "Internal Server Error - Problem with the server."
                        )

                        else -> Log.e("Error", "Unexpected Error: ${response.code()}")
                    }
                    Result.failure(Exception("HTTP error with code ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e("Network error", "Errore nel prendere i dati", e)
                Result.failure(e)
            }
        }
    }
}