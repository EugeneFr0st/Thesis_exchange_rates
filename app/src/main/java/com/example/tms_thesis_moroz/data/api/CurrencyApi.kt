package com.example.tms_thesis_moroz.data.api

import retrofit2.http.GET

interface CurrencyApi {
    @GET("/live?access_key=770a608410223f3d1709ea0988ed1729")    // key
    suspend fun getCurrency(): Currency
}