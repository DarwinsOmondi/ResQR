package com.example.resqr.QRCode

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

class QRCodeViewModel(private val qrCodeRepository: QRCodeRepository) : ViewModel() {
    //generate QR code
    fun generateQRCode(data: String, size: Int = 512): Bitmap {
        return qrCodeRepository.generateQRCode(data, size)
    }
}