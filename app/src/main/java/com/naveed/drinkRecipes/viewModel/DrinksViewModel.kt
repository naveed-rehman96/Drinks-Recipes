package com.naveed.drinkRecipes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.naveed.drinkRecipes.model.DrinkList
import com.naveed.drinkRecipes.repository.DrinksRepository


class DrinksViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: DrinksRepository? = null

    val listOfMovies: MutableLiveData<DrinkList> = MutableLiveData<DrinkList>()


    fun getListByName(drinkName : String) : MutableLiveData<DrinkList?>
    {
        return DrinksRepository().getInstance().getListByName(drinkName)
    }

    fun getListByAlphabet(alphabet : String) : MutableLiveData<DrinkList?>
    {
        return DrinksRepository().getInstance().getListByAlphabet(alphabet)
    }




}