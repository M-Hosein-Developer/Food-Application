package com.example.model

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(version = 1, exportSchema = false, entities = [Food::class])
abstract class Database : RoomDatabase() {

    abstract val foodDao: FoodDao

    companion object {

        private var database: Database? = null
        fun getDatabase(context: Context): Database {

            if (database == null) {

                database = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "Database.db"
                )
                    .allowMainThreadQueries() // روی ترد اصلی اجرا میشه که اصلا توصیه نمیشه
                    .build()

            }
            return database!!
        }

    }

}