package com.example.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.model.NumberReturnModel
import com.example.myapplication.model.SmsModel

@Dao
interface NumberDao {

    @Query("SELECT * FROM NumberReturnModel")
    fun getNumber(): LiveData<NumberReturnModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNumber(vararg numberReturnModel: NumberReturnModel)

    @Query("DELETE FROM NumberReturnModel")
    fun deleteNumber()

    @Query("SELECT * FROM SmsModel")
    fun getSms(): LiveData<List<SmsModel>>

    @Insert
    fun addSms(vararg numberReturnModel: SmsModel)

    @Query("DELETE FROM SmsModel")
    fun deleteAllSms()
}