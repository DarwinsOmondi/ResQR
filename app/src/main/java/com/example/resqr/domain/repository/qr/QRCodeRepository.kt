package com.example.resqr.domain.repository.qr

import com.example.resqr.domain.model.qrModel.QrResponse
import kotlinx.coroutines.flow.Flow

interface QRCodeRepository {
    suspend fun getQRCode(data: String, size: Int = 512): Flow<QrResponse>
}