package com.naveed.drinkRecipes.retrofit

import com.naveed.drinkRecipes.model.DrinkList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCalls {

    @GET("search.php")
    fun searchDrinkByName(@Query("s") name: String): Call<DrinkList>

    @GET("search.php")
    fun searchDrinkFirstLetter(@Query("f") name: String): Call<DrinkList>



}