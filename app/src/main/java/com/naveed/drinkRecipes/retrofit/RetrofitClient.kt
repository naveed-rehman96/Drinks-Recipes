package com.naveed.drinkRecipes.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

    private fun getRetrofitInstance(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
    fun getApiCalls(): ApiCalls {
        return getRetrofitInstance()!!.create(ApiCalls::class.java)
    }
}