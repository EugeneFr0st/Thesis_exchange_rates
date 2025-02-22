package com.example.tms_thesis_moroz.data.repository

import com.example.tms_thesis_moroz.data.dao.CurrencyRatesDao
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity

class CurrencyRatesRepository(private val currencyRatesDao: CurrencyRatesDao) {

    suspend fun addCurrencyRates(currencyRate:CurrencyRatesEntity){
        currencyRatesDao.insertOrUpdate(currencyRate)
    }

    suspend fun getCurrencyRates(): List<CurrencyRatesEntity>{
        return currencyRatesDao.getAllCurrencyRates()
    }

    suspend fun getFavoriteCurrencyRates(userId:String) : List<CurrencyRatesEntity>{
        return currencyRatesDao.getFavoriteCurrencyRates(userId)
    }

    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean) {
        currencyRatesDao.updateFavoriteStatus(id, isFavorite)
    }
}