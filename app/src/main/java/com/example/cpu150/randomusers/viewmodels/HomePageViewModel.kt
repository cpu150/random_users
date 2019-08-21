package com.example.cpu150.randomusers.viewmodels

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cpu150.randomusers.models.GetRandomUsersModel
import com.example.cpu150.randomusers.network.RandomUserEndpoints
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class HomePageViewModel(private val randomUserEndpoints: RandomUserEndpoints) : ViewModel() {

    val dataListLiveData = MutableLiveData<List<HomePageCardViewModel>> ()

    fun getLayoutManager (): RecyclerView.LayoutManager {
        // Need to recreate an instance every time:
        // RecyclerView crashes if trying to set the same LayoutManager instance twice (which happens during a rotation screen for example)
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private val disposable = CompositeDisposable()

    fun requestData (numberOfUser: Int) {
        randomUserEndpoints.getRandomUsers(numberOfUser).also { getUsersSingleObservable ->
            val observer: DisposableSingleObserver<List<HomePageCardViewModel>> = object: DisposableSingleObserver<List<HomePageCardViewModel>>() {
                override fun onError(e: Throwable) {
                    Log.d("HomePage", "Get random users network call failed: " + e.message)
                }

                override fun onSuccess(t: List<HomePageCardViewModel>) {
                    dataListLiveData.value = t
                }
            }

            disposable.add (getUsersSingleObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { getRandomUsersModel ->
                    val array: ArrayList <HomePageCardViewModel> =  ArrayList()
                    getRandomUsersModel.results?.forEach { array.add(HomePageCardViewModel(it)) }
                    array
                }
                .subscribeWith(observer))
        }
    }

    fun parallelRequestsForData (numberOfUser: Int) {
        val observable1 =  randomUserEndpoints.getRandomUsers(numberOfUser/2)
        val observable2 =  randomUserEndpoints.getRandomUsers(numberOfUser/2)
        val requests = ArrayList<Single<GetRandomUsersModel>>()
        observable1.also { requests.add(it) }
        observable2.also { requests.add(it) }

        disposable.add(
            Single.zip(requests) { arrayOfGetRandomUsersModel ->
                val array: ArrayList<HomePageCardViewModel> = ArrayList()
                arrayOfGetRandomUsersModel.forEach { getRandomUsersModel ->
                    (getRandomUsersModel as GetRandomUsersModel).results?.forEach { array.add(HomePageCardViewModel(it)) }
                }
                array
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    dataListLiveData.value =  it
                }) {
                    Log.e("HomePage", "Get random users network call failed $it: " + it.message)
                }
        )
    }

    override fun onCleared() {
        super.onCleared()

        // disposable.clear() ->   will clear all, but can accept new disposable
        // disposable.dispose() -> will clear all and set isDisposed = true, so it will not accept any new disposable
        disposable.clear()
    }
}