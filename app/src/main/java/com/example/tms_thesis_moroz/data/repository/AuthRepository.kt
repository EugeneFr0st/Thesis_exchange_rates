package com.example.tms_thesis_moroz.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepository {
    private val _authState = MutableStateFlow<FirebaseUser?>(null)
    val authState: StateFlow<FirebaseUser?> get() = _authState

    init {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            _authState.value = auth.currentUser
        }
    }

    fun getCurrentUserId(): String? = _authState.value?.uid
}