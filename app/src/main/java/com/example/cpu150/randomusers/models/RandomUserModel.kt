package com.example.cpu150.randomusers.models

import com.google.gson.annotations.SerializedName

data class RandomUserModel (
    @SerializedName("name")
    val name: RandomUserNameModel?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("picture")
    val picture: RandomUserPictureModel?
)
