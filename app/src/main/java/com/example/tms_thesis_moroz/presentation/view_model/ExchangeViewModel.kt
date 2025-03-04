package com.example.tms_thesis_moroz.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity
import com.example.tms_thesis_moroz.data.repository.AuthRepository
import com.example.tms_thesis_moroz.data.repository.CurrencyRatesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class ExchangeViewModel(
    private val repository: CurrencyRatesRepository,
    val authRepository: AuthRepository
) : ViewModel() {

    private val _exchangeList = MutableStateFlow<List<CurrencyRatesEntity>>(emptyList())
    val exchangeList: StateFlow<List<CurrencyRatesEntity>> get() = _exchangeList

    private val _formattedCurrencies = MutableStateFlow<Map<String, String>>(emptyMap())
    val formattedCurrencies: StateFlow<Map<String, String>> get() = _formattedCurrencies

    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> get() = _isDataLoaded

    init {
        viewModelScope.launch {
            authRepository.authState.collect { user ->
                if (user != null) {
                    val userId = authRepository.getCurrentUserId() ?: return@collect
                    fetchAndDisplayCurrency(userId)
                } else {
                    _exchangeList.value = emptyList()
                    _formattedCurrencies.value = emptyMap()
                    _isDataLoaded.value = false
                }
            }
        }

        viewModelScope.launch {
            repository.favoriteStatusChanged.collect {
                val userId = authRepository.getCurrentUserId() ?: return@collect
                val exchangeCurrencies = repository.getCurrencyRates(userId)
                _exchangeList.value = exchangeCurrencies
            }
        }
    }

    fun fetchAndDisplayCurrency(userId: String) {
        if (!_isDataLoaded.value) {
            viewModelScope.launch {
                repository.fetchAndSaveCurrencyRates(userId)
                val exchangeCurrencies = repository.getCurrencyRates(userId)
                val formattedValues = exchangeCurrencies.associate {
                    it.currencyPair to DecimalFormat("#0.00").format(it.exchangeRate)
                }
                _formattedCurrencies.value = formattedValues
                _exchangeList.value = exchangeCurrencies
                _isDataLoaded.value = true
            }
        }
    }

    fun changeFavoriteStatus(id: Int, currentStatus: Boolean, userId: String) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(id, !currentStatus, userId)
        }
    }
}