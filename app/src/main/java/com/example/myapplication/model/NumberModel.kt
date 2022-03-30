package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class NumberModel(
    @SerializedName("user_id") val id: String,
    @SerializedName("country") val country: String,
    @SerializedName("key") val key: String = "",
)
