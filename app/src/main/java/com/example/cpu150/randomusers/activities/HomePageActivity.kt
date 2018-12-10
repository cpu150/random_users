package com.example.cpu150.randomusers.activities

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import com.example.cpu150.randomusers.R
import com.example.cpu150.randomusers.dependencyinjection.ContextModule
import com.example.cpu150.randomusers.dependencyinjection.DaggerHomePageComponent
import kotlinx.android.synthetic.main.activity_home_page.*
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
        val binding = ActivityHomePageBinding.inflate(layoutInflater)
        binding.homePageViewModel = ViewModelProviders.of(this, HomePageViewModelFactory(homePageComponent)).get(HomePageViewModel::class.java)
        binding.setLifecycleOwner(this)

        binding.homePageViewModel?.homePageListAdapter?.observeForever {
            random_person_recycler_view.adapter = it
        }

        // Views setup
        random_person_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }
}
