package com.example.resqr.clienthome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.model.HomeUiState
import com.example.resqr.model.User
import com.example.resqr.model.UserMedicalData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ClientViewmodel(private val clientRepository: ClientRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    //save user medical data
    fun saveClientMedicalData(user: User) {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            val results = clientRepository.saveMedicalData(user)
            _uiState.value = if (results.isSuccess) {
                _uiState.value = HomeUiState(isLoading = false)
                HomeUiState(saveSuccess = "Medical data saved successfully")
            } else {
                _uiState.value = HomeUiState(isLoading = false)
                HomeUiState(error = "Error saving medical data")
            }
        }
    }

    //fetch user medical data
    fun fetchClientMedicalData(userId: UUID) {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            val response = clientRepository.fetchMedicalData(userId)
            _uiState.value = if (response.isSuccess) {
                HomeUiState(fetchSuccess = response.getOrNull())
            } else {
                HomeUiState(error = "Error fetching medical data.")
            }
        }
    }
}