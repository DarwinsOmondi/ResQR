package com.example.resqr.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignInFactory(private val signingRepository: SigningRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewmodel::class.java)){
            return SignInViewmodel(signingRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}