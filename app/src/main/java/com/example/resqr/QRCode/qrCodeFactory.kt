package com.example.resqr.QRCode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class qrCodeFactory(private val qrCodeRepository: QRCodeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QRCodeViewModel::class.java)) {
            return QRCodeViewModel(qrCodeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}