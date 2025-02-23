package com.example.tms_thesis_moroz.presentation.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.tms_thesis_moroz.databinding.FragmentConverterBinding
import com.example.tms_thesis_moroz.presentation.view_model.ConverterViewModel
import com.example.tms_thesis_moroz.presentation.view_model.CurrencySelectionDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat
import kotlin.math.min

class ConverterFragment : Fragment() {

    private val viewModel: ConverterViewModel by viewModel()
    private var _binding: FragmentConverterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedCurrencies.observe(viewLifecycleOwner) { currencies ->
            updateCurrencyFields(currencies)
        }

        viewModel.amounts.observe(viewLifecycleOwner) { amounts ->
            updateAmounts(amounts)
        }

        binding.addCurrencyButton.setOnClickListener {
            showCurrencySelectionDialog()
        }

        binding.confirmButton.setOnClickListener {
            viewModel.convertCurrencies()
        }
    }

    private fun showCurrencySelectionDialog() {
        val allCurrencies = listOf("USD", "EUR", "RUB", "GBP", "JPY")
        val selectedCurrencies = viewModel.selectedCurrencies.value?.toMutableSet() ?: mutableSetOf()

        val dialog = CurrencySelectionDialog(
            requireContext(),
            allCurrencies,
            selectedCurrencies
        ) { newSelection ->
            viewModel.updateSelectedCurrencies(newSelection)
        }
        dialog.show()
    }

    private fun updateCurrencyFields(currencies: Set<String>) {
        val existingFields = binding.currencyContainer.children
            .filterIsInstance<TextInputLayout>()
            .associateBy { it.tag as String }

        currencies.forEach { currency ->
            if (existingFields.containsKey(currency)) return@forEach

            val textInputLayout = TextInputLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                hint = "Введите сумму в $currency"
                tag = currency
            }

            val textInputEditText = TextInputEditText(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                setText("0")
                setupCurrencyInput(currency, this)
            }

            textInputLayout.addView(textInputEditText)
            binding.currencyContainer.addView(textInputLayout)
        }
    }

    private fun updateAmounts(amounts: Map<String, Double>) {
        binding.currencyContainer.children
            .filterIsInstance<TextInputLayout>()
            .forEach { layout ->
                val currency = layout.tag as? String ?: return@forEach
                val editText = layout.editText ?: return@forEach
                val newAmount = amounts[currency] ?: 0.0
                val formattedAmount = formatAmount(newAmount)

                if (!editText.text.isNullOrEmpty()) {
                    editText.setText(formattedAmount)
                }
            }
    }

    private fun setupCurrencyInput(currency: String, textInputEditText: TextInputEditText) {
        textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val amount = s?.toString()?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
                viewModel.updateAmount(currency, amount)
            }
        })
    }

    private fun formatAmount(amount: Double): String {
        return if (amount % 1 == 0.0) amount.toInt().toString() else DecimalFormat("#,##0.00").format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}