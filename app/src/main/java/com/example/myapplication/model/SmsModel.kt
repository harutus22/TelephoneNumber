package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class SmsModel (
	@PrimaryKey(autoGenerate = true) val dbId: Int = 0,
	@SerializedName("id") val id : Int,
	@SerializedName("user_id") val user_id : String,
	@SerializedName("sms_adress") val sms_adress : String,
	@SerializedName("sms_body") val sms_body : String,
	@SerializedName("sms_senttime") val sms_senttime : Long,
	@SerializedName("date") val date : String
)