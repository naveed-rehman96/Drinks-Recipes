package com.naveed.drinkRecipes.di

import android.content.Context
import androidx.room.Room
import com.naveed.drinkRecipes.roomdb.FavouriteDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        FavouriteDB::class.java,
        "FavouriteDB").fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    fun provideYourDao(db: FavouriteDB) = db.favDao()


}