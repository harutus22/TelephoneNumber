package com.example.myapplication.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class NumberReturnModel(
    @PrimaryKey(autoGenerate = true) val dbId: Int = 0,
    @SerializedName("number_id") val id: Int,
    @SerializedName("number_user_id") val userId: String,
    @SerializedName("phone_number") val number: String,
    @SerializedName("last_active") val time: Int,
    val country: String
) : Parcelable
