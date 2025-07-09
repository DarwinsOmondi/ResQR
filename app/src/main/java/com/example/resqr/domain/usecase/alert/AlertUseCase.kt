package com.example.resqr.domain.usecase.alert

data class AlertUseCase(
    val sendAlertUseCase: SendAlertUseCase,
    val getAlertUseCase: GetAlertUseCase,
    val updateAlertUseCase: UpdateAlertUseCase

)
