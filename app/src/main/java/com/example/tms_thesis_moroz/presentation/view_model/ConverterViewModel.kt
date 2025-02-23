package com.example.tms_thesis_moroz.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tms_thesis_moroz.data.repository.AuthRepository
import com.example.tms_thesis_moroz.data.repository.CurrencyRatesRepository
import kotlinx.coroutines.launch

class ConverterViewModel(
    private val authRepository: AuthRepository,
    private val currencyRatesRepository: CurrencyRatesRepository
) : ViewModel() {

    private val _selectedCurrencies = MutableLiveData<Set<String>>(emptySet())
    val selectedCurrencies: LiveData<Set<String>> get() = _selectedCurrencies

    private val _amounts = MutableLiveData<Map<String, Double>>(emptyMap())
    val amounts: LiveData<Map<String, Double>> get() = _amounts

    private val _exchangeRates = MutableLiveData<Map<String, Double>>()
    val exchangeRates: LiveData<Map<String, Double>> get() = _exchangeRates

    private val _inputAmounts = mutableMapOf<String, Double>()
    private var lastInputCurrency: String? = null  // Запоминаем последнюю введенную валюту

    private val userId: String?
        get() = authRepository.getCurrentUserId()

    init {
        loadExchangeRates()
    }

    private fun loadExchangeRates() {
        viewModelScope.launch {
            try {
                val rates = currencyRatesRepository.getCurrencyRates()
                val rateMap = rates.associate { it.currencyPair to it.exchangeRate }
                _exchangeRates.value = rateMap
            } catch (e: Exception) {
                _exchangeRates.value = emptyMap()
            }
        }
    }

    fun updateSelectedCurrencies(currencies: Set<String>) {
        _selectedCurrencies.value = currencies
        _amounts.value = emptyMap() // Сброс значений
        _inputAmounts.clear()
        lastInputCurrency = null
    }

    fun updateAmount(currency: String, amount: Double) {
        _inputAmounts[currency] = amount
        lastInputCurrency = currency  // Обновляем последнюю введенную валюту
    }

    fun convertCurrencies() {
        val exchangeRates = _exchangeRates.value ?: return
        val selectedCurrencies = _selectedCurrencies.value ?: return
        val sourceCurrency = lastInputCurrency ?: return  // Проверяем, была ли выбрана валюта для ввода
        val sourceAmount = _inputAmounts[sourceCurrency] ?: return

        val newAmounts = mutableMapOf<String, Double>()

        selectedCurrencies.forEach { targetCurrency ->
            if (targetCurrency != sourceCurrency) {
                val sourceToUSD = exchangeRates["USD$sourceCurrency"] ?: 1.0
                val usdToTarget = exchangeRates["USD$targetCurrency"] ?: 1.0

                if (sourceToUSD != 0.0 && usdToTarget != 0.0) {
                    val rate = usdToTarget / sourceToUSD
                    newAmounts[targetCurrency] = sourceAmount * rate
                }
            } else {
                newAmounts[targetCurrency] = sourceAmount
            }
        }
        _amounts.value = newAmounts
    }
}