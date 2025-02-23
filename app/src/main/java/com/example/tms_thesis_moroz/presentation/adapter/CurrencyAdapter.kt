package com.example.tms_thesis_moroz.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tms_thesis_moroz.databinding.ItemCurrencyBinding

class CurrencyAdapter(
    private val currencies: List<String>,
    private val selectedCurrencies: MutableSet<String>
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: String) {
            binding.currencyName.text = currency
            binding.currencyCheckBox.isChecked = selectedCurrencies.contains(currency)

            binding.currencyCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedCurrencies.add(currency)
                } else {
                    selectedCurrencies.remove(currency)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencies[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }
}