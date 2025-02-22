package com.example.tms_thesis_moroz.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tms_thesis_moroz.data.api.CurrencyApi
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity
import com.example.tms_thesis_moroz.data.repository.AuthRepository
import com.example.tms_thesis_moroz.data.repository.CurrencyRatesRepository
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class ExchangeViewModel(
    private val repository: CurrencyRatesRepository,
    private val authRepository: AuthRepository,
    private val currencyApi: CurrencyApi
) : ViewModel() {

    private val _exchangeList = MutableLiveData<List<CurrencyRatesEntity>>()
    val exchangeList: LiveData<List<CurrencyRatesEntity>> get() = _exchangeList

    private val _formattedCurrencies = MutableLiveData<Map<String, String>>()
    val formattedCurrencies: LiveData<Map<String, String>> get() = _formattedCurrencies

    fun fetchAndDisplayCurrency() {
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            viewModelScope.launch {

                val currency = currencyApi.getCurrency()
                val quotes = currency.quotes ?: emptyMap()

                val decimalFormat = DecimalFormat("#0.00")
                val formattedValues = mapOf(
                    "USDEUR" to decimalFormat.format(quotes["USDEUR"] ?: 0.0),
                    "USDRUB" to decimalFormat.format(quotes["USDRUB"] ?: 0.0),
                    "USDBYN" to decimalFormat.format(quotes["USDBYN"] ?: 0.0)
                )

                _formattedCurrencies.postValue(formattedValues)

                quotes.entries.forEach { (currencyPair, exchangeRate) ->
                    val currencyRate = CurrencyRatesEntity(
                        userId = userId,
                        currencyPair = currencyPair,
                        exchangeRate = exchangeRate,
                        isFavorite = false
                    )
                    repository.addCurrencyRates(currencyRate)
                }

                val exchangeCurrencies = repository.getCurrencyRates()
                _exchangeList.postValue(exchangeCurrencies)
            }
        }
    }

    fun changeFavoriteStatus(id: Int, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(id, !currentStatus)
            _exchangeList.postValue(repository.getCurrencyRates())
        }
    }
}