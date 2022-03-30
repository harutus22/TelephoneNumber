package com.example.myapplication.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.myapplication.db.NumberDB
import com.example.myapplication.db.dao.NumberDao
import com.example.myapplication.model.NumberReturnModel
import com.example.myapplication.model.SmsModel
import kotlinx.coroutines.*

class NumberRepository(private val app: Application) {
    private lateinit var numberDao: NumberDao
    private lateinit var allNumbers: LiveData<NumberReturnModel>
    private lateinit var allSms: LiveData<List<SmsModel>>

    fun init() {
        val db = NumberDB.getDb(app)
        numberDao = db.taskDao()
        allNumbers = numberDao.getNumber()
        allSms = numberDao.getSms()
    }

    fun insert(number: NumberReturnModel) {
        CoroutineScope(Dispatchers.IO).launch {
            numberDao.addNumber(number)
        }
    }

    fun insertSms(sms: List<SmsModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            for (i in sms){
                numberDao.addSms(i)
            }
        }
    }

    fun getAllSms(): LiveData<List<SmsModel>>{
        return allSms
    }

    fun deleteAllSms(){
        CoroutineScope(Dispatchers.IO).launch {
            numberDao.deleteAllSms()
        }
    }

    fun getNumber(): LiveData<NumberReturnModel> = numberDao.getNumber()

    fun delete() {
        CoroutineScope(Dispatchers.IO).launch {
            numberDao.deleteNumber()
        }
    }
}