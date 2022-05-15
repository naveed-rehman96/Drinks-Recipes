package com.naveed.drinkRecipes.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.naveed.drinkRecipes.model.Drinks

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDrinkFav(drink : Drinks)

    @Delete
    fun deleteRowFav(drink: Drinks)

    @Query("SELECT EXISTS(SELECT * FROM favouritetable WHERE id = :id)")
    fun isRowIsExist(id : Int) : Boolean

    @Query("SELECT * FROM favouritetable")
    fun getAllRecords() : LiveData<List<Drinks>>

    @Query("SELECT * FROM favouritetable ORDER BY RANDOM() LIMIT 1")
    fun getRandomRecord() : LiveData<Drinks>
}