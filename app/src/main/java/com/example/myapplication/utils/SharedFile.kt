package com.example.myapplication.utils

import android.content.Context
import androidx.preference.PreferenceManager
import kotlinx.serialization.descriptors.PrimitiveKind

fun setBoolean(title: String, value: Boolean, context: Context) {
    val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
    editor.putBoolean(title, value)
    editor.apply()
}

fun getBoolean(context: Context, title: String) =
    PreferenceManager.getDefaultSharedPreferences(context).getBoolean(title, false)

fun setString(title: String, value: String, context: Context) {
    val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
    editor.putString(title, value)
    editor.apply()
}

fun getSharedString(context: Context, title: String) =
    PreferenceManager.getDefaultSharedPreferences(context).getString(title, "")

fun setUserId(value: String, context: Context){
    val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
    editor.putString(USER_ID, value)
    editor.apply()
}

fun getUserId(context: Context) =
    PreferenceManager.getDefaultSharedPreferences(context).getString(USER_ID, "")

fun setNumberUserId(value: String, context: Context){
    val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
    editor.putString(NUMBER_USER_ID, value)
    editor.apply()
}

fun getNumberUserId(context: Context) =
    PreferenceManager.getDefaultSharedPreferences(context).getString(NUMBER_USER_ID, "")