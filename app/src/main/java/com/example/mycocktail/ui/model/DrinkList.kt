package com.example.mycocktail.ui.model

import com.example.mycocktail.utils.Constants

class DrinkList {



    fun getDrinksCategory(): MutableList<String>{
        val categoryList = mutableListOf(
            Constants.ALCOHOLIC,
            Constants.NON_ALCOHOLIC
        )
        return categoryList
    }


}