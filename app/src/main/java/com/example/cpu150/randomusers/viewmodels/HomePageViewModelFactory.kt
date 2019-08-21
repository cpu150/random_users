package com.example.cpu150.randomusers.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cpu150.randomusers.dependencyinjection.HomePageComponent

class HomePageViewModelFactory (private val homePageComponent: HomePageComponent) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        val homePageViewModel = HomePageViewModel()
//        homePageComponent.inject(homePageViewModel)
//        return homePageViewModel as T

        return homePageComponent.homeModelView() as T
    }
}