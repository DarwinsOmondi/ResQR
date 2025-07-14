package com.example.resqr.domain.usecase.alert

import com.example.resqr.domain.model.alertModel.Alert
import com.example.resqr.domain.repository.alert.AlertRepository

class SendAlertUseCase(private val alertRepository: AlertRepository) {
    operator fun invoke(alert: Alert) = alertRepository.sendAlert(alert = alert)
}