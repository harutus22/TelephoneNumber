package com.example.myapplication.retrofit

import com.example.myapplication.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiCall {
    @POST("virstapps?action=VN_GET_NUMBERSS&key=07d17890-55cf-4623-ab24-e3cf3f2a83d1")
    fun getNumbersCount(): Call<List<CountryModel>>

    @POST("newuser")
    @Headers("Content-Type: application/json")
    fun postUser(@Body userModel: UserModel): Call<String>

    @POST("getnumber")
    @Headers("Content-Type: application/json")
    fun getNumber(@Body number: NumberModel): Call<NumberReturnModel>

    @POST("getsms/")
    @Headers("Content-Type: application/json")
    fun getSms(@Body userModel: NumberUserModel): Call<List<SmsModel>>
}