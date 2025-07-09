package com.example.resqr.domain.model.alertModel

sealed class AlertResponse {
    object Uninitialized : AlertResponse()
    object Loading : AlertResponse()
    data class Success(val message: String) : AlertResponse()
    data class GetAlert(val alert: Alert?) : AlertResponse()
    data class AlertError(val message: String) : AlertResponse()
}