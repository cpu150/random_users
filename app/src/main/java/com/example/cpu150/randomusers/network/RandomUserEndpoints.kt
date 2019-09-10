package com.example.cpu150.randomusers.network

import com.example.cpu150.randomusers.models.GetRandomUsersModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RandomUserEndpoints {
    @Headers("Content-Type:application/json; Accept:application/json")
    @GET("?format=json")
    fun getRandomUsers (@Query("results") numberOfUser: Int): Single<GetRandomUsersModel>
}
