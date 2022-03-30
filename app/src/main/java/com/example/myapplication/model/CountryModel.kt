package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class CountryModel(
    @SerializedName("country") val country : String,
    @SerializedName("count") val emptyNumbers : Int
)
