package com.example.cpu150.randomusers.models

import com.google.gson.annotations.SerializedName
import java.net.URI

data class RandomUserPictureModel (
    @SerializedName("large")
    val large: URI?,

    @SerializedName("medium")
    val medium: URI?,

    @SerializedName("thumbnail")
    val thumbnail: URI?
)
