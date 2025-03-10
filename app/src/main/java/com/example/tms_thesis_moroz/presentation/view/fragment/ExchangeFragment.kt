package com.example.tms_thesis_moroz.presentation.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tms_thesis_moroz.databinding.FragmentExchangeRatesBinding
import com.example.tms_thesis_moroz.presentation.adapter.ExchangeAdapter
import com.example.tms_thesis_moroz.presentation.view_model.ExchangeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangeFragment : Fragment() {

    private val viewModel: ExchangeViewModel by viewModel()
    private var _binding: FragmentExchangeRatesBinding? = null
    private val binding get() = _binding!!

    private var adapter: ExchangeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExchangeRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ExchangeAdapter(emptyList()) { currency ->
            val userId = viewModel.authRepository.getCurrentUserId() ?: return@ExchangeAdapter
            viewModel.changeFavoriteStatus(currency.id, currency.isFavorite, userId)
        }
        binding.exchangeRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.exchangeRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.exchangeList.collect { currencies ->
                adapter?.updateData(currencies)
            }
        }

        lifecycleScope.launch {
            viewModel.formattedCurrencies.collect { formattedData ->
                binding.EURCost.text = formattedData["USDEUR"] ?: "N/A"
                binding.RUBCost.text = formattedData["USDRUB"] ?: "N/A"
                binding.BYNCost.text = formattedData["USDBYN"] ?: "N/A"
            }
        }

        val userId = viewModel.authRepository.getCurrentUserId() ?: return
        viewModel.fetchAndDisplayCurrency(userId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}