package com.example.resqr.clientprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ClientProfileFactory(private val clientProfileRepository: ClientProfileRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientProfileViewModel::class.java)) {
            return ClientProfileViewModel(clientProfileRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}