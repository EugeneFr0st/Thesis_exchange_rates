package com.example.tms_thesis_moroz.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tms_thesis_moroz.databinding.ItemCurrencyBinding

class CurrencyAdapter(
    var currencies: List<CurrencyQuote>
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencies[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int = currencies.size

    class CurrencyViewHolder(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: CurrencyQuote) {
            binding.currencyName.text = currency.name
            binding.currencyRate.text = currency.rate.toString()
        }
    }
}

data class CurrencyQuote(val name: String, val rate: Double)
