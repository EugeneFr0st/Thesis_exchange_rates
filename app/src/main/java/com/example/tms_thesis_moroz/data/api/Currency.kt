package com.example.tms_thesis_moroz.data.api

data class Currency (
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Int,
    val source: String,
    val quotes: Map<String, Double>
)
