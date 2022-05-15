package com.naveed.drinkRecipes.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.naveed.drinkRecipes.model.DrinkList
import com.naveed.drinkRecipes.retrofit.ApiCalls
import com.naveed.drinkRecipes.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DrinksRepository(var apiCalls: ApiCalls = RetrofitClient.getApiCalls()) {

    private val listOfDrinksByName: MutableLiveData<DrinkList?> = MutableLiveData<DrinkList?>()

    fun getInstance(): DrinksRepository {
        return DrinksRepository()
    }

    fun getListByName(drinkName: String): MutableLiveData<DrinkList?> {
        val call = apiCalls.searchDrinkByName(drinkName)

        call.enqueue(object : Callback<DrinkList> {
            override fun onResponse(call: Call<DrinkList>, response: Response<DrinkList>) {
                listOfDrinksByName.value = response.body()
            }

            override fun onFailure(call: Call<DrinkList>, t: Throwable) {
                listOfDrinksByName.postValue(null)
            }

        })
        return listOfDrinksByName
    }

    fun getListByAlphabet(drinkName: String): MutableLiveData<DrinkList?> {

        if (drinkName.length <= 1) {
            val call = apiCalls.searchDrinkFirstLetter(drinkName)

            call.enqueue(object : Callback<DrinkList> {
                override fun onResponse(call: Call<DrinkList>, response: Response<DrinkList>) {
                    listOfDrinksByName.value = response.body()
                    Log.e("onResponse", "onResponse: ${response.body()}" )
                }

                override fun onFailure(call: Call<DrinkList>, t: Throwable) {
                    listOfDrinksByName.postValue(null)
                    Log.e("onResponse", "onResponse: ${t.message}" )
                }

            })
        }
        return listOfDrinksByName
    }


}