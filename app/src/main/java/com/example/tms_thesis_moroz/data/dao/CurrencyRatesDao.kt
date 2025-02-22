package com.example.tms_thesis_moroz.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity

@Dao
interface CurrencyRatesDao {

    //для записи
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(currencyRate:CurrencyRatesEntity)

    //для получения всех данных
    @Query("SELECT * FROM currency_rates")
    suspend fun getAllCurrencyRates() : List<CurrencyRatesEntity>

    //для получени избранных данных
    @Query("SELECT * FROM currency_rates WHERE is_favorite=1 AND user_id = :userId")
    suspend fun getFavoriteCurrencyRates(userId:String) : List<CurrencyRatesEntity>

    //для обновления данных
    @Query("UPDATE currency_rates SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite:Boolean = false )

}

