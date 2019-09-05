package com.example.cpu150.randomusers.dependencyinjection

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.cpu150.randomusers.BuildConfig
import com.example.cpu150.randomusers.network.RandomUserEndpoints
import com.example.cpu150.randomusers.network.interceptors.NetworkConnectionInterceptor
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
import com.ncornette.cache.OkCacheControl

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
    internal fun okHttpClient (cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor, networkMonitor: OkCacheControl.NetworkMonitor, networkConnectionInterceptor: NetworkConnectionInterceptor): OkHttpClient {
        val okCacheControl = OkCacheControl.on(OkHttpClient.Builder())
            .overrideServerCachePolicy(1, TimeUnit.SECONDS)
            .forceCacheWhenOffline(networkMonitor)

        val okHttpBuilder = okCacheControl.apply()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(networkConnectionInterceptor)
            .cache(cache)

        addHeadersExample(okHttpBuilder)
        setupTimeoutExample(okHttpBuilder)

        return okHttpBuilder.build()
    }

    @Provides
    internal fun networkMonitor(connectivityManager: ConnectivityManager): OkCacheControl.NetworkMonitor {
        return OkCacheControl.NetworkMonitor {
            connectivityManager.activeNetworkInfo?.isConnected == true
        }
    }

    @Provides
    internal fun networkConnectionInterceptor(connectivityManager: ConnectivityManager): NetworkConnectionInterceptor {
        return object : NetworkConnectionInterceptor() {
            override val isInternetAvailable: Boolean
                get() {
                    return connectivityManager.activeNetworkInfo?.isConnected == true
                }

            override fun onDataFromCache() {
                // response came from cache
            }

            override fun onDataFromInternet() {
                // response came from server
            }

            override fun onDataUnavailable() {
                // no internet and no cache data
            }

            override fun onInternetUnavailable() {
                // no internet connection
            }
        }
    }

    private fun addHeadersExample (okHttpBuilder: OkHttpClient.Builder) {
        okHttpBuilder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")

            chain.proceed(requestBuilder.build())
        }
    }

    private fun setupTimeoutExample (okHttpBuilder: OkHttpClient.Builder) {
        val requestTimeout = 30L

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

    @Provides
    internal fun connectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}