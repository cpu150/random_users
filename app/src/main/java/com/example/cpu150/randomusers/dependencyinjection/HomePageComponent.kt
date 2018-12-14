package com.example.cpu150.randomusers.dependencyinjection

import com.example.cpu150.randomusers.adapters.HomePageListAdapter
import com.example.cpu150.randomusers.network.RandomUserEndpoints
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface HomePageComponent {

    fun randomUserEndpoints (): RandomUserEndpoints

    fun homePageListAdapter (): HomePageListAdapter
}