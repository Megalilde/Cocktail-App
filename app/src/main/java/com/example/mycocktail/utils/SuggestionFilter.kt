package com.example.mycocktail.utils

import com.example.mycocktail.ui.viewmodel.DrinkViewModel

class SuggestionFilter (private val viewModel: DrinkViewModel){

    fun getFilteredSuggestions(query: String): List<String>?{
        val allSuggestions = viewModel.suggestionDrink.value
        return allSuggestions?.filter { it.contains(query, ignoreCase = true) }
    }
}