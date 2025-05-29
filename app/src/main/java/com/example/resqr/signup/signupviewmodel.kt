package com.example.resqr.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.model.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SignUpViewmodel(private val signupRepository: SignupRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    //sign up user with email and password
    fun signUpUser(
        email: String,
        fullName: String,
        phoneNumber: String,
        password: String,
        role: String
    ) {
        if (email.isBlank() && fullName.isBlank() && password.isBlank() && phoneNumber.isBlank()) {
            _uiState.value = AuthUiState(error = "None of the fields provided can be empty")
        } else {
            viewModelScope.launch {
                val results =
                    signupRepository.signUpUser(email, fullName, phoneNumber, password, role)
                _uiState.value = if (results.isSuccess) {
                    AuthUiState(success = results.getOrNull())
                } else {
                    AuthUiState(error = results.exceptionOrNull()?.message ?: "Unknown Error")
                }
            }
        }
    }

    fun signUpInstitution(
        email: String,
        institutionName: String,
        institutionPhoneNumber: String,
        password: String,
        role: String,
        region: String
    ) {
        if (
            email.isBlank() &&
            institutionName.isBlank() &&
            institutionPhoneNumber.isBlank() &&
            password.isBlank() &&
            region.isBlank()
        ) {
            _uiState.value = AuthUiState(error = "None of the fields provided can be empty")
        } else {
            viewModelScope.launch {
                val results = signupRepository.signUpInstitution(
                    email,
                    institutionName,
                    institutionPhoneNumber,
                    password,
                    role,
                    region
                )
                _uiState.value = if (results.isSuccess) {
                    AuthUiState(success = results.getOrNull())
                } else {
                    AuthUiState(error = results.exceptionOrNull()?.message ?: "Unknown Error")
                }
            }
        }
    }
}