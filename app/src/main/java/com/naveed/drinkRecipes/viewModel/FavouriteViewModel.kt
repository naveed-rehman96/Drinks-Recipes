package com.naveed.drinkRecipes.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naveed.drinkRecipes.model.Drinks
import com.naveed.drinkRecipes.roomdb.FavouriteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(DelicateCoroutinesApi::class)
@HiltViewModel
class FavouriteViewModel @Inject constructor(private val favouriteDao: FavouriteDao) : ViewModel() {

    var mAllRecords: LiveData<List<Drinks>>? = null


    init {
        GlobalScope.launch(Dispatchers.IO) {
            mAllRecords = favouriteDao.getAllRecords()
            Log.e("All Records", ": $mAllRecords" )
        }

    }

    fun insertFavouriteDrink(drinks: Drinks)
    {
        favouriteDao.insertDrinkFav(drinks)
    }

    fun isRowExist(id: Int) : Boolean
    {
        return  favouriteDao.isRowIsExist(id)
    }
    fun deleteRow(drinks: Drinks)
    {
        return  favouriteDao.deleteRowFav(drinks)
    }
    fun getRandomRecord () : LiveData<Drinks>
    {

        return favouriteDao.getRandomRecord()
    }



}