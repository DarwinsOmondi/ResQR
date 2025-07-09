package com.example.resqr.domain.usecase.alert

import com.example.resqr.domain.model.alertModel.Alert
import com.example.resqr.domain.repository.alertRepository.AlertRepository

class UpdateAlertUseCase(private val alertRepository: AlertRepository) {
    operator fun invoke(alert: Alert) = alertRepository.updateAlert(alert = alert)
}