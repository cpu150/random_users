package com.example.cpu150.randomusers.activities

import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.cpu150.randomusers.R
import com.example.cpu150.randomusers.dependencyinjection.ContextModule
import com.example.cpu150.randomusers.dependencyinjection.DaggerHomePageComponent
import com.example.cpu150.randomusers.databinding.ActivityHomePageBinding
import com.example.cpu150.randomusers.viewmodels.HomePageViewModel
import com.example.cpu150.randomusers.viewmodels.HomePageViewModelFactory

class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Injection dependencies
        val homePageComponent = DaggerHomePageComponent.builder()
            .contextModule(ContextModule(this))
            .build()

        // Setup Bindings
        val binding: ActivityHomePageBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)
        binding.homePageViewModel = ViewModelProviders.of(this, HomePageViewModelFactory(homePageComponent.randomUserEndpoints(), homePageComponent.homePageListAdapter())).get(HomePageViewModel::class.java)
        binding.setLifecycleOwner(this)
    }
}
