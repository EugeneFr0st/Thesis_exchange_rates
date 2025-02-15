package com.example.tms_thesis_moroz.presentation.view.fragment

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tms_thesis_moroz.data.api.CurrencyApi
import com.example.tms_thesis_moroz.databinding.FragmentExchangeRatesBinding
import com.example.tms_thesis_moroz.presentation.adapter.CurrencyAdapter
import com.example.tms_thesis_moroz.presentation.adapter.CurrencyQuote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExchangeFragment : Fragment() {

    private var _binding: FragmentExchangeRatesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExchangeRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.currencyRecyclerView.layoutManager = layoutManager
        val adapter = CurrencyAdapter(emptyList())
        binding.currencyRecyclerView.adapter = adapter

        fetchAndDisplayCurrency(adapter)
    }

    private fun fetchAndDisplayCurrency(adapter: CurrencyAdapter) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.currencylayer.com")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val currencyApi = retrofit.create(CurrencyApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            val currency = currencyApi.getCurrency()
            val quotes = currency.quotes

            val decimalFormat = DecimalFormat("#0.00")
            val usdEur = decimalFormat.format(quotes["USDEUR"] ?: 0.0)
            val usdRub = decimalFormat.format(quotes["USDRUB"] ?: 0.0)
            val usdByn = decimalFormat.format(quotes["USDBYN"] ?: 0.0)

            withContext(Dispatchers.Main) {
                binding.EURCost.text = usdEur
                binding.RUBCost.text = usdRub
                binding.BYNCost.text = usdByn

                adapter.currencies = quotes.entries.map { CurrencyQuote(it.key, it.value) }
                adapter.notifyDataSetChanged()

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
