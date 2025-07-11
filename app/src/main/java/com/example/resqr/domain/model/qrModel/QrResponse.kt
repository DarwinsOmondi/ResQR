package com.example.resqr.domain.model.qrModel

import android.graphics.Bitmap

sealed class QrResponse {
    object Uninitialized : QrResponse()
    object Loading : QrResponse()
    data class GetQr(val qr: Bitmap) : QrResponse()
    data class QrError(val message: String) : QrResponse()
}