package com.example.cpu150.randomusers.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import com.example.cpu150.randomusers.adapters.HomePageListAdapter
import com.example.cpu150.randomusers.dependencyinjection.HomePageComponent
import com.example.cpu150.randomusers.models.GetRandomUsersModel
import com.example.cpu150.randomusers.network.RandomUserEndpoints
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePageViewModel (private var homePageComponent: HomePageComponent): ViewModel() {

    @Inject
    lateinit var randomUserEndpoints: RandomUserEndpoints

    var homePageListAdapter = MutableLiveData<HomePageListAdapter> ()

    private val disposable = CompositeDisposable()

    init {
        homePageComponent.inject(this)

        fetchData()
    }

    private fun fetchData () {
        randomUserEndpoints.getRandomUsers(10).let { getUsersSingleObservable ->

            val observer: DisposableSingleObserver<GetRandomUsersModel> = object: DisposableSingleObserver<GetRandomUsersModel>() {
                override fun onError(e: Throwable) {
                    Log.d("HomePage", "Get random users network call failed: " + e.message)
                }

                override fun onSuccess(t: GetRandomUsersModel) {
                    val adapter = homePageComponent.homePageListAdapter()
                    adapter.dataList = t.results
                    homePageListAdapter.value = adapter
                }
            }

            disposable.add (getUsersSingleObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer))
        }
    }

    override fun onCleared() {
        super.onCleared()

        // disposable.clear() ->   will clear all, but can accept new disposable
        // disposable.dispose() -> will clear all and set isDisposed = true, so it will not accept any new disposable
        disposable.dispose()
    }
}