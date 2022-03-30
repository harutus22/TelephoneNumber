package com.example.myapplication.utils

import com.example.myapplication.model.CountryModel

interface OnCountryChoseClick{
    fun onFlagClick(countryModel: CountryModel)
}