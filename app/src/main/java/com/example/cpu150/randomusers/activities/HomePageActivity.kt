package com.example.cpu150.randomusers.activities

import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.cpu150.randomusers.R
import com.example.cpu150.randomusers.dependencyinjection.ContextModule
import com.example.cpu150.randomusers.dependencyinjection.DaggerHomePageComponent
import com.example.cpu150.randomusers.databinding.ActivityHomePageBinding
import com.example.cpu150.randomusers.viewmodels.HomePageCardViewModel
import com.example.cpu150.randomusers.viewmodels.HomePageViewModel
import com.example.cpu150.randomusers.viewmodels.HomePageViewModelFactory

class HomePageActivity : AppCompatActivity() {

    private val viewModelInitialisedStateKey = "viewModelInitialisedStateKey"
    private var viewModelInitialisedStateValue = false

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(viewModelInitialisedStateKey, viewModelInitialisedStateValue)

        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Restore instance state
        viewModelInitialisedStateValue = savedInstanceState?.getBoolean(viewModelInitialisedStateKey, false) ?: false

        // Injection dependencies
        val homePageComponent = DaggerHomePageComponent.builder()
            .contextModule(ContextModule(this))
            .build()

        // Setup Bindings
        val binding: ActivityHomePageBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)
        binding.viewModel = ViewModelProviders.of(this, HomePageViewModelFactory(homePageComponent.randomUserEndpoints())).get(HomePageViewModel::class.java)
        val homePageListAdapter = homePageComponent.homePageListAdapter()
        binding.listAdapter = homePageListAdapter
        binding.lifecycleOwner = this

        // Bind adapter and viewmodel
        binding.viewModel?.dataListLiveData?.observe(this, Observer<List<HomePageCardViewModel>> {
            homePageListAdapter.dataList = it
            homePageListAdapter.notifyDataSetChanged()
        })

        // Initialise just during the first Activity creation
        if (!viewModelInitialisedStateValue) {
            viewModelInitialisedStateValue = true

            // Request 10 random users
            binding.viewModel?.requestData(10)
        }
    }
}
