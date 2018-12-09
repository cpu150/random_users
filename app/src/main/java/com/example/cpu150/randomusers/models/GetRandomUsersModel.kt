package com.example.cpu150.randomusers.models

import com.google.gson.annotations.SerializedName

data class GetRandomUsersModel (@SerializedName("results") val results: List<RandomUserModel>?)
