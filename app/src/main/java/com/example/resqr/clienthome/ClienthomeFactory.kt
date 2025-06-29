package com.example.resqr.clienthome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class ClientHomeFactory(private val clientRepository: ClientRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientViewmodel::class.java)) {
            return ClientViewmodel(clientRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}