package com.example.cpu150.randomusers.network

import android.content.Context
import com.example.cpu150.randomusers.BuildConfig
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

object RandomUserApi {
    private var okHttp3Downloader: OkHttp3Downloader? = null

    var randomUserEndpoints: RandomUserEndpoints? = null

    fun getPicasso (context: Context?): Picasso? {
        val okHttp3Downloader = this.okHttp3Downloader

        return if (okHttp3Downloader != null && context != null) Picasso.Builder(context).downloader(okHttp3Downloader).build() else null
    }

    fun init (okHttpClient: OkHttpClient, okHttp3Downloader: OkHttp3Downloader)
    {
        this.okHttp3Downloader = okHttp3Downloader

        randomUserEndpoints = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RandomUserEndpoints::class.java)
    }
}