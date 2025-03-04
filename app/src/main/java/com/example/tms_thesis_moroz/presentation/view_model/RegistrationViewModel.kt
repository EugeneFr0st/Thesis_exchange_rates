package com.example.tms_thesis_moroz.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationViewModel() : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> get() = _registrationResult

    private val _navigateToExchange = MutableLiveData<Boolean>()
    val navigateToExchange: LiveData<Boolean> get() = _navigateToExchange

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _registrationResult.value = RegistrationResult(isSuccess = true)
            } else {
                _registrationResult.value = RegistrationResult(isSuccess = false)
            }
        }
    }

    fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _navigateToExchange.value = true
        }
    }

    data class RegistrationResult(val isSuccess: Boolean)
}