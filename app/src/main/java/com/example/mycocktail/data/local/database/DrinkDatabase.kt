package com.example.mycocktail.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mycocktail.data.local.dao.DrinkDao
import com.example.mycocktail.data.datamodels.Drink


@Database(entities = [Drink::class], version = 1)
abstract class DrinkDatabase : RoomDatabase(){

    abstract fun drinkDao(): DrinkDao

    companion object{
        @Volatile
        private var INSTANCE: DrinkDatabase? = null

        fun getInstance(context: Context): DrinkDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DrinkDatabase::class.java,
                        "drink_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}