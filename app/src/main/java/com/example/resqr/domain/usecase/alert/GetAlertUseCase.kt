package com.example.resqr.domain.usecase.alert

import com.example.resqr.domain.repository.alert.AlertRepository

class GetAlertUseCase(private val alertRepository: AlertRepository) {
    operator fun invoke() = alertRepository.getAlerts()
}