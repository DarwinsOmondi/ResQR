package com.example.resqr.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.model.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewmodel(private val signinrepository: SigningRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    //sign in user with email and password
    fun signInUser(email: String, password: String) {
        _uiState.value = AuthUiState(isLoading = true)
        if (email.isBlank() && password.isBlank()) {
            _uiState.value = AuthUiState(error = "Email or Password can not be empty")
        } else {
            viewModelScope.launch {
                val result = signinrepository.signInUser(email, password)
                _uiState.value = (if (result.isSuccess) {
                    AuthUiState(success = result.getOrNull())
                } else {
                    AuthUiState(error = result.exceptionOrNull()?.message ?: "Unknown Error")
                })
            }
        }
    }
}