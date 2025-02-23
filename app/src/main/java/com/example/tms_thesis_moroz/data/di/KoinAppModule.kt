package com.example.tms_thesis_moroz.data.di

import androidx.room.Room
import com.example.tms_thesis_moroz.data.api.CurrencyApi
import com.example.tms_thesis_moroz.data.dao.CurrencyRatesDB
import com.example.tms_thesis_moroz.data.repository.AuthRepository
import com.example.tms_thesis_moroz.data.repository.CurrencyRatesRepository
import com.example.tms_thesis_moroz.presentation.view_model.ConverterViewModel
import com.example.tms_thesis_moroz.presentation.view_model.ExchangeViewModel
import com.example.tms_thesis_moroz.presentation.view_model.FavoriteViewModel
import com.example.tms_thesis_moroz.presentation.view_model.LoginViewModel
import com.example.tms_thesis_moroz.presentation.view_model.RegistrationViewModel
import com.example.tms_thesis_moroz.presentation.view_model.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Room.databaseBuilder(get(), CurrencyRatesDB::class.java, "Currency_Rates")
            .fallbackToDestructiveMigration()
            .build()
            .currencyRatesDao()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.currencylayer.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
    }

    single { FirebaseAuth.getInstance() }
    single { AuthRepository(get()) }
    single { CurrencyRatesRepository(get()) }

    viewModel { RegistrationViewModel() }
    viewModel { LoginViewModel() }
    viewModel { ExchangeViewModel(get(), get()) }
    viewModel { FavoriteViewModel(get(), get()) }
    viewModel { ConverterViewModel(get(), get()) }
    viewModel { SettingsViewModel() }
}
