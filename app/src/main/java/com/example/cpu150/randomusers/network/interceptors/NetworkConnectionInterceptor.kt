package com.example.cpu150.randomusers.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

abstract class NetworkConnectionInterceptor : Interceptor {
    abstract val isInternetAvailable: Boolean

    abstract fun onInternetUnavailable()
    abstract fun onDataFromCache()
    abstract fun onDataFromInternet()
    abstract fun onDataUnavailable()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        when {
            response.networkResponse() != null -> onDataFromInternet()
            response.cacheResponse() != null -> onDataFromCache()
            else -> onDataUnavailable()
        }

        if (!isInternetAvailable) {
            onInternetUnavailable()
        }

        return response
    }
}