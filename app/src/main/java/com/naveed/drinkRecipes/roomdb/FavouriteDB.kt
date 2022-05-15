package com.naveed.drinkRecipes.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.naveed.drinkRecipes.model.Drinks
import java.util.concurrent.Executors



@Database(entities = [Drinks::class], version = 1)
abstract class FavouriteDB : RoomDatabase() {

    abstract fun favDao(): FavouriteDao

    companion object {
        private var instance: FavouriteDB? = null

        @Synchronized
        fun getInstance(ctx: Context): FavouriteDB {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, FavouriteDB::class.java,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }

    }



}