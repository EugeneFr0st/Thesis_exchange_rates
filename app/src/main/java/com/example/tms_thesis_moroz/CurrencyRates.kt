package com.example.tms_thesis_moroz

import android.app.Application
import com.example.tms_thesis_moroz.data.di.appModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class CurrencyRates: Application() {

    override fun onCreate(){
        super.onCreate()

        FirebaseApp.initializeApp(this)
        startKoin {
            androidLogger()
            androidContext(this@CurrencyRates)
            modules(appModule)
        }
    }
}