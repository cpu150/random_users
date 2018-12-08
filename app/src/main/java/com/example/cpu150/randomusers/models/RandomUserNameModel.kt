package com.example.cpu150.randomusers.models

import com.google.gson.annotations.SerializedName

class RandomUserNameModel (
    @SerializedName ("title")
    val title: String?,

    @SerializedName ("first")
    val first: String?,

    @SerializedName ("last")
    val last: String?
)
