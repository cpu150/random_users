package com.example.cpu150.randomusers.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import com.example.cpu150.randomusers.R
import com.example.cpu150.randomusers.adapters.HomePageListAdapter
import com.example.cpu150.randomusers.dependencyinjection.ContextModule
import com.example.cpu150.randomusers.dependencyinjection.DaggerHomePageComponent
import com.example.cpu150.randomusers.models.GetRandomUsersModel
import com.example.cpu150.randomusers.network.RandomUserEndpoints
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home_page.*
import javax.inject.Inject

class HomePageActivity : AppCompatActivity() {

    @Inject
    lateinit var randomUserEndpoints: RandomUserEndpoints

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val homePageComponent = DaggerHomePageComponent.builder()
            .contextModule(ContextModule(this))
            .build()

        homePageComponent
            .inject(this)

        randomUserEndpoints.getRandomUsers(10).let { getUsersSingleObservable ->

            val observer: DisposableSingleObserver<GetRandomUsersModel> = object: DisposableSingleObserver<GetRandomUsersModel>() {
                override fun onError(e: Throwable) {
                    Log.d("HomePageActivity", "Get random users network call failed: " + e.message)
                }

                override fun onSuccess(t: GetRandomUsersModel) {
                    val homePageListAdapter = homePageComponent.homePageListAdapter()

                    homePageListAdapter.dataList = t.results

                    random_person_recycler_view.apply {
                        setHasFixedSize(true)
                        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        adapter = homePageListAdapter
                    }
                }
            }

            // Execute the request
            disposable.add (getUsersSingleObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // disposable.clear() ->   will clear all, but can accept new disposable
        // disposable.dispose() -> will clear all and set isDisposed = true, so it will not accept any new disposable
        disposable.dispose()
    }
}
