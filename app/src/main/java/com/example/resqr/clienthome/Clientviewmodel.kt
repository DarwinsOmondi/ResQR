package com.example.resqr.clienthome

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.model.UiState
import com.example.resqr.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ClientViewmodel(private val clientRepository: ClientRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

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
}