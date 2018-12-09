package com.example.cpu150.randomusers.dependencyinjection

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule constructor(private val context: Context) {

    @Provides
    fun context(): Context {
        return context.applicationContext
    }
}