package com.example.cpu150.randomusers.dependencyinjection

import com.example.cpu150.randomusers.activities.HomePageActivity
import com.example.cpu150.randomusers.adapters.HomePageListAdapter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface HomePageComponent {
    fun inject(activity: HomePageActivity)

    fun homePageListAdapter (): HomePageListAdapter
}