package com.example.tms_thesis_moroz.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tms_thesis_moroz.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel() : ViewModel() {

    private val _navigationEvent = MutableStateFlow<Int?>(null)
    val navigationEvent: StateFlow<Int?> get() = _navigationEvent

    fun logout() {
        viewModelScope.launch {
            FirebaseAuth.getInstance().signOut()
            _navigationEvent.value = R.id.action_settings_to_login
        }
    }
}