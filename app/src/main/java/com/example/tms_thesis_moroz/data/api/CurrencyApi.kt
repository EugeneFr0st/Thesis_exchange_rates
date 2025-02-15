package com.example.tms_thesis_moroz.data.api

import retrofit2.http.GET

interface CurrencyApi {
    @GET("/live?access_key=2dd44bec055eefceeb974b4c28abe49f")    // key
    suspend fun getCurrency(): Currency
}