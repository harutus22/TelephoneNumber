package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.db.dao.NumberDao
import com.example.myapplication.model.NumberReturnModel
import com.example.myapplication.model.SmsModel

@Database(entities = [NumberReturnModel::class, SmsModel::class], version = 2)
abstract class NumberDB : RoomDatabase() {
    abstract fun taskDao(): NumberDao

    companion object {
        lateinit var numberDb: NumberDB
        fun getDb(context: Context): NumberDB {
            if (!this::numberDb.isInitialized)
                numberDb = Room.databaseBuilder(
                    context.applicationContext,
                    NumberDB::class.java,
                    "country_number_db.db"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build()
            return numberDb
        }
    }
}