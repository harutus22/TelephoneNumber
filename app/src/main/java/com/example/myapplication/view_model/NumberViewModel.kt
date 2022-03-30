package com.example.myapplication.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myapplication.model.NumberReturnModel
import com.example.myapplication.model.SmsModel

class NumberViewModel(application: Application): AndroidViewModel(application) {
    private var repo: NumberRepository = NumberRepository(application)
    private lateinit var sms: LiveData<List<SmsModel>>
    private lateinit var number: LiveData<NumberReturnModel>

    init {
        repo.init()
        sms = repo.getAllSms()
        number = repo.getNumber()
    }

    fun insert(number: NumberReturnModel){
        repo.insert(number)
    }

    fun delete(){
        repo.delete()
    }

    fun getSms(): LiveData<List<SmsModel>> = sms

    fun getNumber() = number

    fun insertSms(list: List<SmsModel>){
        repo.insertSms(list)
    }

    fun deleteSms(){
        repo.deleteAllSms()
    }


}