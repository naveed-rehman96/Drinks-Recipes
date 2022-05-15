package com.naveed.drinkRecipes.interfaceRcv

interface OnFavouriteClickListener {
    fun onFavouriteItemClickListener(pos : Int) : Boolean
    fun onRemoveFavouriteClickListener(pos : Int) : Boolean
}