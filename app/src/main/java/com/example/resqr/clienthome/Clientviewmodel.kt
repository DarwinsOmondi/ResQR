package com.example.resqr.clienthome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.model.Alert
import com.example.resqr.model.UiState
import com.example.resqr.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ClientViewmodel(private val clientRepository: ClientRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _timeLeft = MutableStateFlow(5)
    val timeLeft: StateFlow<Int> = _timeLeft

    private val _countdownFinished = MutableStateFlow(false)
    val countdownFinished: StateFlow<Boolean> = _countdownFinished

    private val _hasStarted = MutableStateFlow(false)
    val hasStarted: StateFlow<Boolean> = _hasStarted

    //save user medical data
    fun saveClientMedicalData(user: User) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            val results = clientRepository.saveMedicalData(user)
            _uiState.value = if (results.isSuccess) {
                _uiState.value = UiState(isLoading = false)
                UiState(saveSuccess = "Medical data saved successfully")
            } else {
                _uiState.value = UiState(isLoading = false)
                UiState(error = "Error saving medical data")
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(error = null, saveSuccess = null)
    }

    fun startCountdown() {
        _hasStarted.value = true
        clientRepository.alertCountDown(
            onTick = { seconds ->
                _timeLeft.value = seconds
            },
            onFinish = {
                _countdownFinished.value = true
                viewModelScope.launch {
                    delay(3000)
                    resetCountdown()
                }
            }
        )
    }

    fun resetCountdown() {
        _timeLeft.value = 5
        _hasStarted.value = false
        _countdownFinished.value = false
    }

    //send alert
    fun sendEmergencyAlert(alert: Alert) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            val results = clientRepository.sendEmergencyAlert(alert)
            _uiState.value = if (results.isSuccess) {
                _uiState.value = UiState(isLoading = false)
                UiState(saveSuccess = "Alert sent successfully")
            } else {
                _uiState.value = UiState(isLoading = false)
                UiState(error = "Error sending alert")
            }
        }
    }


    //fetch user alert data
    fun fetchAlertData(userid: String) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            val results = clientRepository.fetchAlertData(userid)
            _uiState.value = if (results.isSuccess) {
                _uiState.value = UiState(isLoading = false)
                UiState(alertSuccess = results.getOrNull())
            } else {
                _uiState.value = UiState(isLoading = false)
                UiState(error = "Error fetching alert data")
            }
        }
    }
}