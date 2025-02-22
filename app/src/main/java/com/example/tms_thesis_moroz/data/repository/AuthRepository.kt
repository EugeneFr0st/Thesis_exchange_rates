package com.example.tms_thesis_moroz.data.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository(private val firebaseAuth:FirebaseAuth) {
     fun getCurrentUserId(): String?{
        return firebaseAuth.currentUser?.uid
     }
}