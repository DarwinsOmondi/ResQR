package com.example.resqr.presentation.viewmodel

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.domain.model.alertModel.Alert
import com.example.resqr.domain.model.alertModel.AlertResponse
import com.example.resqr.domain.model.alertModel.AlertType
import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.model.medicalRecordModel.EmergencyContact
import com.example.resqr.domain.usecase.alert.AlertUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel(private val alertUseCase: AlertUseCase) : ViewModel() {
    private val _alertState = MutableStateFlow<AlertResponse>(AlertResponse.Uninitialized)
    val alertState: MutableStateFlow<AlertResponse> = _alertState

    private val _longitude = MutableStateFlow<Double?>(null)
    val longitude: StateFlow<Double?> = _longitude

    private val _latitude = MutableStateFlow<Double?>(null)
    val latitude: StateFlow<Double?> = _latitude

    private val _victimLocation = MutableStateFlow<String?>(null)
    val victimLocation: StateFlow<String?> = _victimLocation

    fun updateVictimLocation(location: String) {
        _victimLocation.value = location
    }

    fun updateLocation(lat: Double, lon: Double) {
        _latitude.value = lat
        _longitude.value = lon
    }


    private val _timeLeft = MutableStateFlow(30)
    val timeLeft: StateFlow<Int> = _timeLeft

    private val _countdownFinished = MutableStateFlow(false)
    val countdownFinished: StateFlow<Boolean> = _countdownFinished

    private val _hasStarted = MutableStateFlow(false)
    val hasStarted: StateFlow<Boolean> = _hasStarted

    private var countdownTimer: CountDownTimer? = null


    private val _shouldSendAlert = MutableStateFlow(false)
    val shouldSendAlert: StateFlow<Boolean> = _shouldSendAlert

    fun startCountdown(totalTime: Int = 30) {
        _hasStarted.value = true
        _countdownFinished.value = false

        countdownTimer?.cancel()

        countdownTimer = object : CountDownTimer(totalTime * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                _countdownFinished.value = true
                viewModelScope.launch {
                    delay(100)
                    _hasStarted.value = false
                    delay(3000)
                    resetCountdown()
                }
            }
        }.start()
    }


    fun stopCountdownEarly() {
        countdownTimer?.cancel()
        countdownTimer = null
        resetCountdown()
    }

    private fun resetCountdown() {
        _timeLeft.value = 30
        _hasStarted.value = false
        _countdownFinished.value = false
    }


    fun triggerSendAlert(
        user: User,
        contact: EmergencyContact,
        latitude: String,
        longitude: String
    ) {
        val alert = Alert(
            userId = user.id,
            victimName = user.fullName,
            victimPhoneNumber = user.phoneNumber,
            victimLatitude = latitude,
            victimLongitude = longitude,
            victimEmergencyContact = contact,
            alertType = AlertType.GENERAL
        )
        sendAlert(alert)
        _shouldSendAlert.value = true
    }

    fun sendAlert(alert: Alert) {
        viewModelScope.launch {
            _alertState.value = AlertResponse.Loading
            alertUseCase.sendAlertUseCase(alert).collect { result ->
                _alertState.value = result
            }
        }
    }

    fun getAlert() {
        viewModelScope.launch {
            _alertState.value = AlertResponse.Loading
            alertUseCase.getAlertUseCase().collect { result ->
                _alertState.value = result
            }
        }
    }

    fun updateAlert(alert: Alert) {
        viewModelScope.launch {
            _alertState.value = AlertResponse.Loading
            alertUseCase.updateAlertUseCase(alert).collect { result ->
                _alertState.value = result
            }
        }
    }

    fun onDirectVictim(text: String, textToSpeech: TextToSpeech?, context: Context) {
        val params = Bundle().apply {
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 0.8f)
            putFloat(TextToSpeech.Engine.KEY_PARAM_PAN, -0.5f)
        }
        if (text.isNotEmpty()) {
            textToSpeech?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                params,
                null
            )
        }
    }
}