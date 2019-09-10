package com.example.cpu150.randomusers.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetRandomUsersModel (
    @Expose(serialize = true, deserialize = true)
    @SerializedName("results")
    val results: List<RandomUserModel>?)
