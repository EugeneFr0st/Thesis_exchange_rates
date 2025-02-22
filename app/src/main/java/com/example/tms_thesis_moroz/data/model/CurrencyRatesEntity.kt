package com.example.tms_thesis_moroz.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "currency_rates")
data class CurrencyRatesEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "currency_pair") val currencyPair: String,
    @ColumnInfo(name = "exchange_rate") val exchangeRate: Double,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean
)