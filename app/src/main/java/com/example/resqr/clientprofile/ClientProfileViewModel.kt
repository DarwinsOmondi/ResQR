package com.example.resqr.clientprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ClientProfileViewModel(private val repository: ClientProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    //fetch user medical data
    fun fetchClientMedicalData(userId: String) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            val response = repository.fetchMedicalData(userId)
            _uiState.value = if (response.isSuccess) {
                UiState(fetchSuccess = response.getOrNull())
            } else {
                UiState(error = "Error fetching medical data.")
            }
        }
    }

    //signout user
    fun signOut() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            val response = repository.signOutUser()
            _uiState.value = if (response.isSuccess) {
                UiState(saveSuccess = "Successfully signed out.")
            } else {
                UiState(error = "Error signing out.")
            }
        }
    }
}