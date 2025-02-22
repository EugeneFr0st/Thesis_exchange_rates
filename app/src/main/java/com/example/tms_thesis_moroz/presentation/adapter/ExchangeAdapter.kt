package com.example.tms_thesis_moroz.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tms_thesis_moroz.R
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity
import com.example.tms_thesis_moroz.databinding.ItemExchangeBinding

class ExchangeAdapter(
    private var currencies: List<CurrencyRatesEntity>,
    private val onFavoriteClick: (CurrencyRatesEntity) -> Unit
) : RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    class ExchangeViewHolder(
        private val binding: ItemExchangeBinding,
        private val onFavoriteClick: (CurrencyRatesEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: CurrencyRatesEntity) {
            binding.currencyName.text = currency.currencyPair
            binding.currencyRate.text = currency.exchangeRate.toString()

            binding.starButton.setImageResource(
                if (currency.isFavorite) R.drawable.gold_star else R.drawable.star
            )

            binding.starButton.setOnClickListener {
                onFavoriteClick(currency)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val binding = ItemExchangeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExchangeViewHolder(binding, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        holder.bind(currencies[position])
    }

    override fun getItemCount(): Int = currencies.size

    fun updateData(newList: List<CurrencyRatesEntity>) {
        currencies = newList
        notifyDataSetChanged()
    }
}

