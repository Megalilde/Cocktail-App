package com.example.mycocktail.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycocktail.data.datamodels.Drink
import com.example.mycocktail.data.remote.model.DrinkId
import com.example.mycocktail.repository.DrinkRepository
import com.example.mycocktail.ui.model.DrinkList
import com.example.mycocktail.utils.Constants
import kotlinx.coroutines.launch

class DrinkViewModel: ViewModel() {

    private val _drinkListLocal = DrinkList()
    private var drinkRepository: DrinkRepository = DrinkRepository()

    private val _categoryDrinks = MutableLiveData<MutableList<String>>()
    val categoryDrinks: MutableLiveData<MutableList<String>> get() = _categoryDrinks

    /*-------------------------------------------------------------------------------------------*/

    private val _drinkList = MutableLiveData<Result<MutableList<Drink>?>>()
    val drinkList : MutableLiveData<Result<MutableList<Drink>?>> get() = _drinkList

    private val _drinkDetail = MutableLiveData<Result<MutableList<DrinkId>?>>()
    val drinkDetail: MutableLiveData<Result<MutableList<DrinkId>?>> get() = _drinkDetail

    // Variabile booleana stato della chiamata.
    private val _retryStatus = MutableLiveData<Boolean>()
    val retryStatus: MutableLiveData<Boolean> get() = _retryStatus

    private var currentCategoryType: String = Constants.ALCOHOLIC
    /*-------------------------------------------------------------------------------------------*/

    private val _suggestionDrink = MutableLiveData<MutableList<String>?>()
    val suggestionDrink: MutableLiveData<MutableList<String>?> get() = _suggestionDrink

    /*-------------------------------------------------------------------------------------------*/
    private val _favoriteDrinkList = MutableLiveData<MutableList<Drink>?>()
    val favoriteDrinkList: MutableLiveData<MutableList<Drink>?> get() = _favoriteDrinkList
    /*-------------------------------------------------------------------------------------------*/



    fun loadSuggestionName() {
        val currentSuggestions = _suggestionDrink.value?.toMutableList() ?: mutableListOf()
        for (drink in _drinkList.value?.getOrNull() ?: mutableListOf()) {
            if (!currentSuggestions.contains(drink.strDrink)) {
                currentSuggestions.add(drink.strDrink)
            }
        }
        _suggestionDrink.value = currentSuggestions
    }

    fun initDatabaseContext(context: Context) {
        drinkRepository.initDatabaseRepositoryContext(context)
    }


    fun initRepoContext(context: Context){
        drinkRepository.initSharedContext(context)
    }


    fun initMethods(){
        fetchDrinksFromRepository()
        _categoryDrinks.value = _drinkListLocal.getDrinksCategory()
    }


    fun changeCategory(newType:  String){
        currentCategoryType = newType
        fetchDrinksFromRepository()
    }

    fun fetchDrinksFromRepository(){
        val job = viewModelScope.launch{
            val result = drinkRepository.fetchDrinksFromService(currentCategoryType)
            _drinkList.value = result
            _retryStatus.value = result.isSuccess
        }
        if(job.isCompleted){
            loadSuggestionName()
        }
    }

    fun fetchDrinkByIdFromRepository(id: String){
        viewModelScope.launch{
            val result = drinkRepository.fetchDrinkByIdFromService(id)
            _drinkDetail.value = result
            _retryStatus.value = result.isSuccess
        }

    }

    fun fetchDrinkByNameFromRepository(name: String){
        viewModelScope.launch {
            val result = drinkRepository.fetchDrinkByName(name)
            _drinkDetail.value = result
            _retryStatus.value = result.isSuccess

        }
    }

    fun addFavoriteDrinkFromApi(model: Drink){
        viewModelScope.launch {
            drinkRepository.addDrinkToFavoriteDB(model)
        }
    }

    fun removeFavoriteDrinkFromApi(model: Drink){
        viewModelScope.launch {
            drinkRepository.removeDrinkToFavoriteDB(model)
        }
    }

    fun fetchFavoriteDrinksFromApi(){
        viewModelScope.launch {
            _favoriteDrinkList.value = drinkRepository.fetchFavoriteDrinks()
        }

    }

    /*
    // Misto shared e room
    fun addFavoritesDrinksFromApi(model: Drink){
        viewModelScope.launch {
            drinkRepository.addFavoriteDrinkDB(model, currentCategoryType)
        }

    }

    // Misto shared e room
    fun removeFavoritesDrinksFromApi(model: Drink){
        viewModelScope.launch {
            drinkRepository.removeFavoriteDrinkDB(model, currentCategoryType)
        }
    }

    // Misto shared e room
    fun fetchFavoriteDrinksFromApi(){
        viewModelScope.launch {
            _favoriteDrinkList.value = drinkRepository.fetchFavoriteDrinksFromDB()
        }

    }
    */
    /*
    // Solo Room
    fun addFavoritesDrinks(model: Drink){
        drinkRepository.addFavoriteDrink(model, currentCategoryType)
    }

    // Solo Room
    fun removeFavoritesDrinks(model: Drink){
        drinkRepository.removeFavoriteDrink(model, currentCategoryType)
    }
    // Solo Room
    fun fetchFavoriteDrinks(){
        _favoriteDrinkList.value = drinkRepository.fetchFavoriteDrinks()
    }
    */
}