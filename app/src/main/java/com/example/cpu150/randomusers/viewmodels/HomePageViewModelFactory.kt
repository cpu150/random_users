package com.example.cpu150.randomusers.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.cpu150.randomusers.dependencyinjection.HomePageComponent

class HomePageViewModelFactory (private val homePageComponent: HomePageComponent) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomePageViewModel(homePageComponent) as T
    }
}