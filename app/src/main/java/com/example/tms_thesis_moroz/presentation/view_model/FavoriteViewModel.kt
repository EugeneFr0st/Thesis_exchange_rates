package com.example.tms_thesis_moroz.presentation.view_model

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity
import com.example.tms_thesis_moroz.data.repository.AuthRepository
import com.example.tms_thesis_moroz.data.repository.CurrencyRatesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: CurrencyRatesRepository,
    val authRepository: AuthRepository
) : ViewModel() {

    private val _favoriteList = MutableStateFlow<List<CurrencyRatesEntity>>(emptyList())
    val favoriteList: StateFlow<List<CurrencyRatesEntity>> get() = _favoriteList

    init {
        viewModelScope.launch {
            authRepository.authState.collect { user ->
                if (user != null) {
                    refreshFavorites()
                } else {
                    _favoriteList.value = emptyList()
                }
            }
        }

        viewModelScope.launch {
            repository.favoriteStatusChanged.collect {
                refreshFavorites()
            }
        }
    }

    private fun refreshFavorites() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            _favoriteList.value = repository.getFavoriteCurrencyRates(userId)
        }
    }

    fun changeFavoriteStatus(id: Int, currentStatus: Boolean, userId: String) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(id, !currentStatus, userId)
        }
    }

    fun shareCurrency(currency: CurrencyRatesEntity, context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Текущий курс ${currency.currencyPair}: ${currency.exchangeRate}")
        }
        context.startActivity(Intent.createChooser(shareIntent, ""))
    }

    fun fetchAndDisplayFavorites() {
        refreshFavorites()
    }
}