package com.example.resqr.responderhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ResponderFactory(private val responderRepository: ResponderRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResponderViewModel::class.java)) {
            return ResponderViewModel(responderRepository) as T

        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}