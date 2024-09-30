package com.example.mycocktail.repository

import android.content.Context
import android.util.Log
import com.example.mycocktail.data.local.database.DrinkDatabase
import com.example.mycocktail.data.datamodels.Drink
import com.example.mycocktail.data.remote.model.DrinkId
import com.example.mycocktail.data.remote.model.DrinkResponse
import com.example.mycocktail.data.remote.retrofit.DrinkService
import com.example.mycocktail.preferences.SharedPreferencesManager
import com.example.mycocktail.ui.model.DrinkResponseLocal
import com.google.gson.Gson

class DrinkRepository {
    private val _drinkService = DrinkService()
    private var sharedPreferencesManager: SharedPreferencesManager = SharedPreferencesManager()
    private lateinit var db: DrinkDatabase


    fun initSharedContext(context: Context){
        sharedPreferencesManager.initSharedWithContext(context)
    }
    fun initDatabaseRepositoryContext(context: Context) {
        db = DrinkDatabase.getInstance(context)
    }

    suspend fun fetchDrinksFromService(type: String): Result<MutableList<Drink>?> {
        val drinksDao = db.drinkDao().getDrinksByType(type) ?: mutableListOf()
        if(drinksDao.isEmpty()){
            val response = _drinkService.fetchDrinksFromApi(type)
            if (response.isSuccess) {
                val drinkResponse = response.getOrNull()
                for (drink in drinkResponse?.drinks ?: mutableListOf()) {
                    db.drinkDao().insert(Drink(strDrink = drink.strDrink, strDrinkThumb = drink.strDrinkThumb, idDrink = drink.idDrink, type = type))
                }
                return Result.success(drinkResponse?.drinks?.toMutableList() ?: mutableListOf())
            } else {
                return Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
            }
        }else{
            return Result.success(db.drinkDao().getDrinksByType(type))
        }
    }

    /*suspend fun fetchDrinksFromService(type: String): Result<MutableList<Drink>?> {
        return if (sharedPreferencesManager.isSharedPreferenceTypeIsEmpty(type)) {

            val response = _drinkService.fetchDrinksFromApi(type)
            if (response.isSuccess) {
                val drinkResponse = response.getOrNull()
                val drinkResponseJsonString = Gson().toJson(drinkResponse)

                sharedPreferencesManager.setSharedPreferencesType(type, drinkResponseJsonString)

                Result.success(drinkResponse?.drinks?.toMutableList() ?: mutableListOf())
            } else {
                Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } else {
            val drinkListString = sharedPreferencesManager.getSharedPreferenceType(type)
            val drinkListTemp = Gson().fromJson(drinkListString, DrinkResponse::class.java)
            Result.success(drinkListTemp.drinks.toMutableList())
        }
    }*/

    suspend fun fetchDrinkByIdFromService(id: String): Result<MutableList<DrinkId>?> {
        return try {
            val response = _drinkService.fetchDrinkByIdFromApi(id)
            if (response.isSuccess) {
                val drinkResponse = response.getOrNull()
                Result.success(drinkResponse?.drinks?.toMutableList() ?: mutableListOf())
            } else {
                Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun fetchDrinkByName(name: String): Result<MutableList<DrinkId>?>{
        return try {
            val response = _drinkService.fetchDrinkByNameFromApi(name)
            if (response.isSuccess){
                val drinkResponse = response.getOrNull()
                Result.success(drinkResponse?.drinks?.toMutableList() ?: mutableListOf() )
            }else{
                Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun addDrinkToFavoriteDB(model: Drink){
        db.drinkDao().updateDrinkById(model.idDrink, true)
    }

    suspend fun removeDrinkToFavoriteDB(model: Drink){
        db.drinkDao().updateDrinkById(model.idDrink, false)
    }

    suspend fun fetchFavoriteDrinks():MutableList<Drink>{
        return db.drinkDao().getFavoriteDrinks() ?: mutableListOf()
    }

    // Misto con shared e Room
    /*suspend fun addFavoriteDrinkDB(model: Drink, currentCategoryType: String){
        val drinkListTypeString = sharedPreferencesManager.getSharedPreferenceType(currentCategoryType)
        val drinkListTypeTemp: DrinkResponseLocal = if (drinkListTypeString.isNullOrEmpty()) {
            DrinkResponseLocal()  // Inizializza una lista vuota
        } else {
            Gson().fromJson(drinkListTypeString, DrinkResponseLocal::class.java)
        }
        for (drink in drinkListTypeTemp.drinks) {
            if (drink.idDrink == model.idDrink) {
                drink.isFavorite = true
                db.drinkDao().insert(Drink(idDrink = drink.idDrink, strDrink = drink.strDrink, strDrinkThumb = drink.strDrinkThumb, isFavorite = drink.isFavorite))
            }
        }
        val drinkResponseTypeJsonString = Gson().toJson(drinkListTypeTemp)
        sharedPreferencesManager.setSharedPreferencesType(
            currentCategoryType,
            drinkResponseTypeJsonString
        )
    }

    // Misto con shared e room
    suspend fun removeFavoriteDrinkDB(model: Drink, currentCategoryType: String) {
        val drinkListTypeString =
            sharedPreferencesManager.getSharedPreferenceType(currentCategoryType)
        val drinkListTypeTemp: DrinkResponseLocal = if (drinkListTypeString.isNullOrEmpty()) {
            DrinkResponseLocal()  // Inizializza una lista vuota
        } else {
            Gson().fromJson(drinkListTypeString, DrinkResponseLocal::class.java)
        }
        for (drink in drinkListTypeTemp.drinks) {
            if (drink.idDrink == model.idDrink) {
                drink.isFavorite = false
                db.drinkDao().deleteById(model.idDrink)
            }
        }
        val drinkResponseTypeJsonString = Gson().toJson(drinkListTypeTemp)
        sharedPreferencesManager.setSharedPreferencesType(
            currentCategoryType,
            drinkResponseTypeJsonString
        )
    }

    // Misto shared e room
    suspend fun fetchFavoriteDrinksFromDB(): MutableList<Drink>?{
        return db.drinkDao().getDrinksFavorite()
    }*/


    // Solo shared
    /*fun addFavoriteDrink(model: Drink, currentCategoryType: String) {
        val drinkListTypeString = sharedPreferencesManager.getSharedPreferenceType(currentCategoryType)
        val drinkListTypeTemp: DrinkResponseLocal = if (drinkListTypeString.isNullOrEmpty()) {
            DrinkResponseLocal()  // Inizializza una lista vuota
        } else {
            Gson().fromJson(drinkListTypeString, DrinkResponseLocal::class.java)
        }
        for (drink in drinkListTypeTemp.drinks) {
            if (drink.idDrink == model.idDrink) {
                drink.isFavorite = true
                val drinkListFavoriteString = sharedPreferencesManager.getSharedPreferencesFavorite()

                // Se la stringa è vuota, inizializza una lista vuota, altrimenti deserializza la lista esistente
                val drinkListFavoriteTemp: DrinkResponseLocal = if (drinkListFavoriteString.isNullOrEmpty()) {
                    DrinkResponseLocal()  // Inizializza una lista vuota
                } else {
                    Gson().fromJson(drinkListFavoriteString, DrinkResponseLocal::class.java)
                }

                // Aggiungo il nuovo drink alla lista
                drinkListFavoriteTemp.drinks.add(drink)

                // Converto la lista aggiornata in JSON
                val drinkResponseFavoriteJsonString = Gson().toJson(drinkListFavoriteTemp)

                // Salvo il JSON aggiornato nelle SharedPreferences
                sharedPreferencesManager.setSharedPreferencesFavorite(drinkResponseFavoriteJsonString)
            }
        }
        val drinkResponseTypeJsonString = Gson().toJson(drinkListTypeTemp)
        Log.e("BUBO", drinkResponseTypeJsonString)
        sharedPreferencesManager.setSharedPreferencesType(
            currentCategoryType,
            drinkResponseTypeJsonString
        )
        sharedPreferencesManager.getSharedPreferenceType(currentCategoryType)
            ?.let { Log.e("BUBA", it) }
        Log.e("SETTATO", "Ho risettato lo shared preferences")
    }

    // Solo shared
    fun removeFavoriteDrink(model: Drink, currentCategoryType: String) {
        val drinkListTypeString =
            sharedPreferencesManager.getSharedPreferenceType(currentCategoryType)
        val drinkListTypeTemp: DrinkResponseLocal = if (drinkListTypeString.isNullOrEmpty()) {
            DrinkResponseLocal()  // Inizializza una lista vuota
        } else {
            Gson().fromJson(drinkListTypeString, DrinkResponseLocal::class.java)
        }
        for (drink in drinkListTypeTemp.drinks) {
            if (drink.idDrink == model.idDrink) {
                val drinkListFavoriteString = sharedPreferencesManager.getSharedPreferencesFavorite()
                drink.isFavorite = false

                // Se la stringa è vuota, inizializza una lista vuota, altrimenti deserializza la lista esistente
                val drinkListFavoriteTemp: DrinkResponseLocal = if (drinkListFavoriteString.isNullOrEmpty()) {
                    DrinkResponseLocal()  // Inizializza una lista vuota
                } else {
                    Gson().fromJson(drinkListFavoriteString, DrinkResponseLocal::class.java)
                }

                //drinkListFavoriteTemp.drinks.remove(drink)
                drinkListFavoriteTemp.drinks.removeAll { it.idDrink == model.idDrink }

                // Converto la lista aggiornata in JSON
                val drinkResponseFavoriteJsonString = Gson().toJson(drinkListFavoriteTemp)

                Log.e("PREFERITI", drinkResponseFavoriteJsonString)
                // Salvo il JSON aggiornato nelle SharedPreferences
                sharedPreferencesManager.setSharedPreferencesFavorite(drinkResponseFavoriteJsonString)

            }
        }
        val drinkResponseTypeJsonString = Gson().toJson(drinkListTypeTemp)
        sharedPreferencesManager.setSharedPreferencesType(
            currentCategoryType,
            drinkResponseTypeJsonString
        )
    }


    // Solo shared
    fun fetchFavoriteDrinks(): MutableList<Drink>{
        val drinkListString = sharedPreferencesManager.getSharedPreferencesFavorite()

        val drinkListTemp: DrinkResponseLocal = if (drinkListString.isNullOrEmpty()) {
            DrinkResponseLocal()  // Ritorna una lista vuota
        } else {
            Gson().fromJson(drinkListString, DrinkResponseLocal::class.java)
        }
        return drinkListTemp.drinks
    }*/


}