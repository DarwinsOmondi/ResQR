package com.example.resqr.responderhome

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.model.ResponderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ResponderViewModel(private val repository: ResponderRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ResponderUiState())
    val uiState: StateFlow<ResponderUiState> = _uiState


    init {
        fetchAlertData()
    }

    fun fetchAlertData() {
        viewModelScope.launch {
            _uiState.value = ResponderUiState(isLoading = true)
            val results = repository.fetchAllAlerts()
            _uiState.value = if (results.isSuccess) {
                ResponderUiState(
                    isLoading = false,
                    fetchSuccess = results.getOrNull() ?: emptyList()
                )
            } else {
                ResponderUiState(
                    isLoading = false,
                    error = results.exceptionOrNull()?.message ?: "Error fetching alert data"
                )
            }
        }
    }

    fun respondToAlert(alertId: Int) {
        viewModelScope.launch {
            _uiState.value = ResponderUiState(isLoading = true)
            val results = repository.respondToAlert(alertId)
            _uiState.value = if (results.isSuccess) {
                fetchAlertData() // Refresh alerts
                ResponderUiState(
                    isLoading = false,
                    respondSuccess = "Alert resolved successfully"
                )
            } else {
                ResponderUiState(
                    isLoading = false,
                    error = results.exceptionOrNull()?.message ?: "Error resolving alert"
                )
            }
        }
    }

    fun getAddressFromLatLng(
        latitude: Double,
        longitude: Double,
        context: Context
    ): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                addresses[0].getAddressLine(0) ?: "No address found"
            } else {
                "No address found"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Unable to get address (network error)"
        } catch (e: Exception) {
            e.printStackTrace()
            "Error getting address"
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateTimeDifference(alertSentAt: String): String {
        return try {
            val sentInstant = Instant.parse(alertSentAt)
            val now = Instant.now()
            val duration = Duration.between(sentInstant, now)
            when {
                duration.toMinutes() < 1 -> "Just now"
                duration.toHours() < 1 -> "${duration.toMinutes()} minutes ago"
                duration.toDays() < 1 -> "${duration.toHours()} hours ago"
                else -> "${duration.toDays()} days ago"
            }
        } catch (e: Exception) {
            "Unknown time"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTimeFormatted(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
        return currentTime.format(formatter)
    }
}