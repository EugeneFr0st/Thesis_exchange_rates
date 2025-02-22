package com.example.tms_thesis_moroz.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity

@Database (entities = [CurrencyRatesEntity::class], version = 2, exportSchema = false)
abstract class CurrencyRatesDB:RoomDatabase(){
    abstract fun currencyRatesDao():CurrencyRatesDao
}