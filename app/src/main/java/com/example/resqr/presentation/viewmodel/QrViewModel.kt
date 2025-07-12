package com.example.resqr.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.domain.model.medicalRecordModel.UserWithMedicalData
import com.example.resqr.domain.model.qrModel.QrResponse
import com.example.resqr.domain.usecase.qr.QrCodeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream

class QrViewModel(private val qrCodeUseCase: QrCodeUseCase) : ViewModel() {
    private val _qrState = MutableStateFlow<QrResponse>(QrResponse.Uninitialized)
    val qrState: MutableStateFlow<QrResponse> = _qrState

    private val _userWithMedicalData = MutableStateFlow<UserWithMedicalData?>(null)
    val userWithMedicalData: MutableStateFlow<UserWithMedicalData?> = _userWithMedicalData
    private val _isServiceRunning = MutableStateFlow(false)
    val isServiceRunning: StateFlow<Boolean> = _isServiceRunning
    private val _isNotificationEnabled = MutableStateFlow(false)
    val isNotificationEnabled: StateFlow<Boolean> = _isNotificationEnabled

    fun setIsServiceRunning(value: Boolean) {
        _isServiceRunning.value = value
    }

    fun setIsNotificationEnabled(value: Boolean) {
        _isNotificationEnabled.value = value
    }


    fun getUserQrCode(userWithMedicalData: UserWithMedicalData) {
        viewModelScope.launch(Dispatchers.IO) {
            _qrState.value = QrResponse.Loading
            try {
                val json = Json.encodeToString(userWithMedicalData)
                qrCodeUseCase.invoke(
                    data = json,
                    size = 512
                ).collect { result ->
                    _qrState.value = result
                }
            } catch (e: Exception) {
                _qrState.value = QrResponse.QrError("Failed to generate QR: ${e.message}")
            }
        }
    }

    fun populateUserWithMedicalData(userWithMedicalData: UserWithMedicalData?) {
        _userWithMedicalData.value = userWithMedicalData
    }

    fun saveQRCodeToDevice(bitmap: Bitmap, context: Context) {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "medical_qr_code_${System.currentTimeMillis()}.png"
        )
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            android.widget.Toast.makeText(
                context,
                "QR Code saved to gallery",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            android.widget.Toast.makeText(
                context,
                "Failed to save QR Code",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun shareQRCode(bitmap: Bitmap, context: Context) {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "medical_qr_code_${System.currentTimeMillis()}.png"
        )
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/png"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
        } catch (e: Exception) {
            android.widget.Toast.makeText(
                context,
                "Failed to share QR Code",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }
}