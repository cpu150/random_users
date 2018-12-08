package com.example.cpu150.randomusers

import android.app.Application
import java.io.File
import okhttp3.logging.HttpLoggingInterceptor
import android.util.Log
import com.example.cpu150.randomusers.network.RandomUserApi
import com.squareup.picasso.OkHttp3Downloader
import okhttp3.*

class RandomUsersApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val cacheFile = File(cacheDir, "HttpCache")
        cacheFile.mkdirs()

        val cache = Cache(cacheFile, 10 * 1000 * 1000) //10 MB

        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d("HttpLog", message) })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient()
            .newBuilder()
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val okHttpDownloader = OkHttp3Downloader(okHttpClient)

        RandomUserApi.init(okHttpClient, okHttpDownloader)
    }
}