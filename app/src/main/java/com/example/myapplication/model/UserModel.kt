package com.example.myapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel (
    @SerializedName("user_id") val userId: String = "1231241243123123",
    @SerializedName("sub_status") var subStatus : Int = 0,
    @SerializedName("android_version") val androidVersion : String = "9",
    @SerializedName("device_model") val deviceModel : String = "Test",
    @SerializedName("sim_country") val simCountry : String = "am",
    @SerializedName("token") val token : String = "",
    @SerializedName("version") val version : Int = 1,
    @SerializedName("key") val key : String = ""
): Parcelable
