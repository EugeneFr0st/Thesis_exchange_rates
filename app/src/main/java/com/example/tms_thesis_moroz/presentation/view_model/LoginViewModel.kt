package com.example.tms_thesis_moroz.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel():ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult

    private val _navigateToExchange = MutableLiveData<Boolean>()
    val navigateToExchange: LiveData<Boolean> get() = _navigateToExchange

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _loginResult.value = LoginResult(isSuccess = true)
            } else {
                _loginResult.value = LoginResult(isSuccess = false)
            }
        }
    }

    fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _navigateToExchange.value = true
        }
    }

    data class LoginResult(val isSuccess: Boolean)

}