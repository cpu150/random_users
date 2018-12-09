package com.example.cpu150.randomusers.dependencyinjection

import android.content.Context
import android.util.Log
import com.example.cpu150.randomusers.BuildConfig
import com.example.cpu150.randomusers.network.RandomUserEndpoints
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [(ContextModule::class)])
class NetworkModule {

    @Provides
    @Singleton
    internal fun randomUserEndpoints(okHttpClient: OkHttpClient): RandomUserEndpoints {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RandomUserEndpoints::class.java)
    }

    @Provides
    @Singleton
    internal fun picasso(okHttp3Downloader: OkHttp3Downloader, context: Context): Picasso {
        return Picasso.Builder(context)
            .downloader(okHttp3Downloader)
            .build()
    }

    @Provides
    internal fun okHttp3Downloader (okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }

    @Provides
    @Singleton
    internal fun okHttpClient (cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpBuilder = OkHttpClient()
            .newBuilder()
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor)

        addHeadersExample(okHttpBuilder)

        setupTimeoutExample(okHttpBuilder)

        return okHttpBuilder.build()
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

    @Provides
    internal fun cache(cacheFile: File): Cache {
        return Cache(cacheFile, (10 * 1000 * 1000).toLong()) //10 MB
    }

    @Provides
    internal fun file(context: Context): File {
        val file = File(context.cacheDir, "HttpCache")
        file.mkdirs()
        return file
    }

    @Provides
    internal fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d("HttpLog", message) })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
}