package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class NumberUserModel(
    @SerializedName("number_user_id") val id: String = "",
    @SerializedName("key") val key: String = ""
)
