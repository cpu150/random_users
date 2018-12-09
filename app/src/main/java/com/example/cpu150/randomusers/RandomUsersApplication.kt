package com.example.cpu150.randomusers

import android.app.Application
import java.io.File
import okhttp3.logging.HttpLoggingInterceptor
import android.util.Log
import com.example.cpu150.randomusers.network.RandomUserApi
import com.squareup.picasso.OkHttp3Downloader
import okhttp3.*
import java.util.concurrent.TimeUnit

class RandomUsersApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val cacheFile = File(cacheDir, "HttpCache")
        cacheFile.mkdirs()

        val cache = Cache(cacheFile, 10 * 1000 * 1000) //10 MB

        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d("HttpLog", message) })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpBuilder = OkHttpClient()
            .newBuilder()
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor)

        addHeadersExample(okHttpBuilder)

        setupTimeoutExample(okHttpBuilder)

        val okHttpClient = okHttpBuilder.build()
        val okHttpDownloader = OkHttp3Downloader(okHttpClient)

        RandomUserApi.init(okHttpClient, okHttpDownloader)
    }

    private fun addHeadersExample (okHttpBuilder: OkHttpClient.Builder)
    {
        okHttpBuilder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")

            chain.proceed(requestBuilder.build())
        }
    }

    private fun setupTimeoutExample (okHttpBuilder: OkHttpClient.Builder)
    {
        val requestTimeout = 60L

        okHttpBuilder
            .connectTimeout(requestTimeout, TimeUnit.SECONDS)
            .readTimeout(requestTimeout, TimeUnit.SECONDS)
            .writeTimeout(requestTimeout, TimeUnit.SECONDS)
    }
}