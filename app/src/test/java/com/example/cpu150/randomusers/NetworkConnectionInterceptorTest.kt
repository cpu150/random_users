package com.example.cpu150.randomusers

import com.example.cpu150.randomusers.network.interceptors.NetworkConnectionInterceptor
import okhttp3.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class NetworkConnectionInterceptorTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var chain: Interceptor.Chain

    @Mock
    private lateinit var request: Request

    @Mock
    private lateinit var response: Response

    private var internetAvailable = true

    private var onInternetUnavailable = false
    private var onDataFromCache = false
    private var onDataFromInternet = false
    private var onDataUnavailable = false
    private val networkConnectionInterceptor = object: NetworkConnectionInterceptor() {
        override val isInternetAvailable: Boolean
            get() = internetAvailable

        override fun onInternetUnavailable() {
            onInternetUnavailable = true
        }
        override fun onDataFromCache() {
            onDataFromCache = true
        }
        override fun onDataFromInternet() {
            onDataFromInternet = true
        }
        override fun onDataUnavailable() {
            onDataUnavailable = true
        }
    }

    @Test
    fun testDataFromInternet() {
        Mockito.`when`(response.networkResponse()).thenReturn(response)
        Mockito.`when`(chain.request()).thenReturn(request)
        Mockito.`when`(chain.proceed(request)).thenReturn(response)
        internetAvailable = true

        networkConnectionInterceptor.intercept(chain)

        assertFalse(onInternetUnavailable)
        assertFalse(onDataFromCache)
        assertTrue(onDataFromInternet)
        assertFalse(onDataUnavailable)
    }

    @Test
    fun testDataFromCache() {
        Mockito.`when`(response.cacheResponse()).thenReturn(response)
        Mockito.`when`(chain.request()).thenReturn(request)
        Mockito.`when`(chain.proceed(request)).thenReturn(response)
        internetAvailable = false

        networkConnectionInterceptor.intercept(chain)

        assertTrue(onInternetUnavailable)
        assertTrue(onDataFromCache)
        assertFalse(onDataFromInternet)
        assertFalse(onDataUnavailable)
    }

    @Test
    fun testNoData() {
        Mockito.`when`(chain.request()).thenReturn(request)
        Mockito.`when`(chain.proceed(request)).thenReturn(response)
        internetAvailable = false

        networkConnectionInterceptor.intercept(chain)

        assertTrue(onInternetUnavailable)
        assertFalse(onDataFromCache)
        assertFalse(onDataFromInternet)
        assertTrue(onDataUnavailable)
    }
}