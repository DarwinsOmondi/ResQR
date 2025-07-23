package com.example.resqr.data.repositoryImpl

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.example.resqr.domain.model.qrModel.QrResponse
import com.example.resqr.domain.repository.qr.QRCodeRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QrRepository : QRCodeRepository {
    override suspend fun getQRCode(
        data: String,
        size: Int
    ): Flow<QrResponse> = flow {
        emit(QrResponse.Loading)
        try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size)
            val bitmap = createBitmap(size, size, Bitmap.Config.RGB_565)

            for (x in 0 until size) {
                for (y in 0 until size) {
                    bitmap[x, y] =
                        if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                }
            }
            emit(QrResponse.GetQr(bitmap))
        } catch (e: Exception) {
            emit(QrResponse.QrError(e.message.toString()))
        }
    }
}