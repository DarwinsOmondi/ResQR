package com.example.resqr.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignupFactory(private val signupRepository: SignupRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewmodel::class.java)) {
            return SignUpViewmodel(signupRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}