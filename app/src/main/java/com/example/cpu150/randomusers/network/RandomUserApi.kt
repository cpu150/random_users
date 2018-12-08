package com.example.cpu150.randomusers.network

import com.example.cpu150.randomusers.BuildConfig
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

object RandomUserApi {
    private val randomUserApi: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val randomUserEndpoints: RandomUserEndpoints = randomUserApi.create(RandomUserEndpoints::class.java)
}