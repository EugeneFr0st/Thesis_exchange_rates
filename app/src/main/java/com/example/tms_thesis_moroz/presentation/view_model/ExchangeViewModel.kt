package com.example.tms_thesis_moroz.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity
import com.example.tms_thesis_moroz.data.repository.AuthRepository
import com.example.tms_thesis_moroz.data.repository.CurrencyRatesRepository
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class ExchangeViewModel(
    private val repository: CurrencyRatesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _exchangeList = MutableLiveData<List<CurrencyRatesEntity>>()
    val exchangeList: LiveData<List<CurrencyRatesEntity>> get() = _exchangeList

    private val _formattedCurrencies = MutableLiveData<Map<String, String>>()
    val formattedCurrencies: LiveData<Map<String, String>> get() = _formattedCurrencies

    private val _isDataLoaded = MutableLiveData<Boolean>(false)
    val isDataLoaded: LiveData<Boolean> get() = _isDataLoaded

    fun fetchAndDisplayCurrency() {
        if (_isDataLoaded.value != true) {
            val userId = authRepository.getCurrentUserId()
            if (userId != null) {
                viewModelScope.launch {
                    val exchangeCurrencies = repository.getCurrencyRates()

                    val formattedValues = exchangeCurrencies.associate {
                        it.currencyPair to DecimalFormat("#0.00").format(it.exchangeRate)
                    }

                    _formattedCurrencies.postValue(formattedValues)
                    _exchangeList.postValue(exchangeCurrencies)
                    _isDataLoaded.postValue(true)
                }
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