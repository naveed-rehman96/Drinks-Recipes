package com.naveed.drinkRecipes.model

import com.google.gson.annotations.SerializedName


data class DrinkList (
  @SerializedName("drinks" ) var drinks : ArrayList<Drinks> = arrayListOf()
)