package com.example.cpu150.randomusers.network

import com.example.cpu150.randomusers.models.GetRandomUsersModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserEndpoints {
    @GET("?format=json")
    fun getRandomUsers (@Query("results") numberOfUser: Int): Call<GetRandomUsersModel>
}