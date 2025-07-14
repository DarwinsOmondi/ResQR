package com.example.resqr.domain.repository.alert

import com.example.resqr.domain.model.alertModel.Alert
import com.example.resqr.domain.model.alertModel.AlertResponse
import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    fun sendAlert(alert: Alert): Flow<AlertResponse>
    fun getAlerts(): Flow<AlertResponse>
    fun updateAlert(alert: Alert): Flow<AlertResponse>
}