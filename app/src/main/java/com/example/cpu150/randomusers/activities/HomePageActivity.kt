package com.example.cpu150.randomusers.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import com.example.cpu150.randomusers.R
import com.example.cpu150.randomusers.adapters.HomePageListAdapter
import com.example.cpu150.randomusers.models.GetRandomUsersModel
import com.example.cpu150.randomusers.network.RandomUserApi
import kotlinx.android.synthetic.main.activity_home_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val getUsersCall = RandomUserApi.randomUserEndpoints.getRandomUsers(10)
        getUsersCall.enqueue(object: Callback<GetRandomUsersModel> {
            override fun onFailure(call: Call<GetRandomUsersModel>, t: Throwable) {
                Log.d("HomePageActivity", "Get random users network call failed: " + t.message)
            }

            override fun onResponse(call: Call<GetRandomUsersModel>, response: Response<GetRandomUsersModel>) {
                runOnUiThread {

                    response.body()?.results?.let {
                        random_person_recycler_view.apply {
                            setHasFixedSize(true)
                            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                            adapter = HomePageListAdapter(it, applicationContext)
                        }
                    }
                }
            }
        })
    }
}
