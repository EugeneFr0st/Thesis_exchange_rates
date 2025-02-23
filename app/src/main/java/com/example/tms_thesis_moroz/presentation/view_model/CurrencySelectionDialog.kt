package com.example.tms_thesis_moroz.presentation.view_model

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tms_thesis_moroz.databinding.DialogCurrencySelectionBinding
import com.example.tms_thesis_moroz.presentation.adapter.CurrencyAdapter

class CurrencySelectionDialog(
    private val context: Context,
    private val allCurrencies: List<String>,
    private val selectedCurrencies: MutableSet<String>,
    private val onSave: (Set<String>) -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogCurrencySelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCurrencySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.currencyRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.currencyRecyclerView.adapter = CurrencyAdapter(allCurrencies, selectedCurrencies)

        binding.saveButton.setOnClickListener {
            onSave(selectedCurrencies)
            dismiss()
        }
    }
}