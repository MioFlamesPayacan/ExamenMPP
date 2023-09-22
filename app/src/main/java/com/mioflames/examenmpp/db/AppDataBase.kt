package com.mioflames.examenmpp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//Clase appdatabase que contendrá la base de datos.

@Database(entities = [Lugares::class], version = 1)
abstract class AppDataBase : RoomDatabase(){

    abstract fun lugaresDao(): LugaresDao

    //Singleton
    companion object{
        @Volatile
        private var BASE_DATOS: AppDataBase?=null

        fun getInstance(context: Context):AppDataBase{
            return BASE_DATOS ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "LugaresBD.bd"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { BASE_DATOS = it }
            }
        }
    }
}