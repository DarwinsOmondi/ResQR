package com.example.resqr.domain.usecase.qr

import com.example.resqr.domain.repository.qr.QRCodeRepository

class QrCodeUseCase(private val qrCodeRepository: QRCodeRepository) {
    suspend operator fun invoke(data: String, size: Int = 512) =
        qrCodeRepository.getQRCode(data = data, size = size)
}