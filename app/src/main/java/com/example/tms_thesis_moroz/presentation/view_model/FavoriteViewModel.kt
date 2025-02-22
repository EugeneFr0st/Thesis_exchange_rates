package com.example.tms_thesis_moroz.presentation.view_model

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity
import com.example.tms_thesis_moroz.data.repository.AuthRepository
import com.example.tms_thesis_moroz.data.repository.CurrencyRatesRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: CurrencyRatesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _favoriteList = MutableLiveData<List<CurrencyRatesEntity>>()
    val favoriteList: LiveData<List<CurrencyRatesEntity>> get() = _favoriteList

    fun updateFavoriteStatus(currency: CurrencyRatesEntity) {
        val currentStatus = currency.isFavorite
        viewModelScope.launch {
            repository.updateFavoriteStatus(currency.id, !currentStatus)
            fetchAndDisplayFavorites()
        }
    }

    fun shareCurrency(currency: CurrencyRatesEntity, context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Курс ${currency.currencyPair}: ${currency.exchangeRate}")
        }
        context.startActivity(Intent.createChooser(shareIntent, ""))
    }

    fun fetchAndDisplayFavorites() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            val favoriteCurrencies = repository.getFavoriteCurrencyRates(userId)
            _favoriteList.postValue(favoriteCurrencies)
        }
    }
}
