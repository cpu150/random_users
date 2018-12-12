package com.example.cpu150.randomusers.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cpu150.randomusers.adapters.HomePageListAdapter
import com.example.cpu150.randomusers.network.RandomUserEndpoints

class HomePageViewModelFactory (private val randomUserEndpoints: RandomUserEndpoints, private val listAdapter: HomePageListAdapter) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomePageViewModel(randomUserEndpoints, listAdapter) as T
    }
}