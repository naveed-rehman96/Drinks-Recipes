package com.naveed.drinkRecipes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "FavouriteTable")
data class Drinks(

    @PrimaryKey
    @SerializedName("idDrink")
    var id: Int = 0,

    @SerializedName("strDrink")
    var name: String? = "",

    @SerializedName("strCategory")
    var category: String? = "",

    @SerializedName("strAlcoholic")
    var alcoholic: String? = "",

    @SerializedName("strGlass")
    var glass: String? = "",

    @SerializedName("strInstructions")
    var instruction: String? = "",

    @SerializedName("strDrinkThumb")
    var image: String? = "",

    var imageOffline: String? = "",



)