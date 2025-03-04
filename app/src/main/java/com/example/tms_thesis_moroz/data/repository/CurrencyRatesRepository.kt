package com.example.tms_thesis_moroz.data.repository

import com.example.tms_thesis_moroz.data.api.CurrencyApi
import com.example.tms_thesis_moroz.data.dao.CurrencyRatesDao
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class CurrencyRatesRepository(
    private val currencyRatesDao: CurrencyRatesDao,
    private val currencyApi: CurrencyApi
) {

    private val _favoriteStatusChanged = MutableSharedFlow<Unit>(replay = 1)
    val favoriteStatusChanged: SharedFlow<Unit> get() = _favoriteStatusChanged

    suspend fun fetchAndSaveCurrencyRates(userId: String) {
        val currencyResponse = currencyApi.getCurrency()

        val quotes = currencyResponse.quotes ?: return

        val currencyRatesEntities = quotes.map { (currencyPair, rate) ->
            CurrencyRatesEntity(
                id = 0,
                userId = userId,
                currencyPair = currencyPair,
                exchangeRate = rate,
                isFavorite = false
            )
        }
        currencyRatesEntities.forEach { currencyRate ->
            currencyRatesDao.insertOrUpdate(currencyRate)
        }
    }

    suspend fun getCurrencyRates(userId: String): List<CurrencyRatesEntity> {
        return currencyRatesDao.getAllCurrencyRates(userId)
    }

    suspend fun getFavoriteCurrencyRates(userId: String): List<CurrencyRatesEntity> {
        return currencyRatesDao.getFavoriteCurrencyRates(userId)
    }

    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean, userId: String) {
        currencyRatesDao.updateFavoriteStatus(id, isFavorite, userId)
        _favoriteStatusChanged.emit(Unit)
    }
}