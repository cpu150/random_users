package com.example.cpu150.randomusers.viewmodels

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cpu150.randomusers.adapters.HomePageListAdapter
import com.example.cpu150.randomusers.models.GetRandomUsersModel
import com.example.cpu150.randomusers.network.RandomUserEndpoints
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class HomePageViewModel (private val randomUserEndpoints: RandomUserEndpoints, val listAdapter: HomePageListAdapter): ViewModel() {

    fun getLayoutManager (): RecyclerView.LayoutManager {
            // Need to recreate an instance every time:
            // RecyclerView crashes if trying to set the same LayoutManager instance twice (which happens during a rotation screen for example)
            return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

    private val disposable = CompositeDisposable()

    init {
        // Request data
        fetchData()
    }

    private fun fetchData () {
        randomUserEndpoints.getRandomUsers(10).also { getUsersSingleObservable ->

            val observer: DisposableSingleObserver<GetRandomUsersModel> = object: DisposableSingleObserver<GetRandomUsersModel>() {
                override fun onError(e: Throwable) {
                    Log.d("HomePage", "Get random users network call failed: " + e.message)
                }

                override fun onSuccess(t: GetRandomUsersModel) {
                    listAdapter.dataList = t.results
                    listAdapter.notifyDataSetChanged()
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
        disposable.clear()
    }
}