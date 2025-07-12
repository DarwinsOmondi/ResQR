package com.example.resqr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.domain.model.passwordModel.PasswordResponse
import com.example.resqr.domain.usecase.password.PasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PasswordViewModel(private val passwordUseCase: PasswordUseCase) : ViewModel() {
    private val _passwordState = MutableStateFlow<PasswordResponse>(PasswordResponse.Uninitialized)
    val passwordState: StateFlow<PasswordResponse> = _passwordState

    private val _password = MutableStateFlow<String?>(null)
    val password: StateFlow<String?> = _password

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun setPassword(userId: Int, password: String) {
        viewModelScope.launch {
            _passwordState.value = PasswordResponse.Loading
            passwordUseCase.savePassword(
                userId = userId,
                password = password
            ).collect { result ->
                _passwordState.value = result
            }
        }
    }

    fun getPassword(userId: Int) {
        viewModelScope.launch {
            _passwordState.value = PasswordResponse.Loading
            passwordUseCase.getPassword(userId).collect { result ->
                _passwordState.value = result
            }
        }
    }

    fun deletePassword(userId: Int) {
        viewModelScope.launch {
            _passwordState.value = PasswordResponse.Loading
            passwordUseCase.deletePassword(userId).collect { result ->
                _passwordState.value = result
            }
        }
    }

    fun updatePassword(userId: Int, password: String) {
        viewModelScope.launch {
            _passwordState.value = PasswordResponse.Loading
            passwordUseCase.updatePassword(userId, password).collect { result ->
                _passwordState.value = result
            }
        }
    }

    fun isPasswordCorrect(userId: Int, password: String) {
        viewModelScope.launch {
            _passwordState.value = PasswordResponse.Loading
            passwordUseCase.isPasswordCorrect(userId, password).collect { result ->
                _passwordState.value = result
            }
        }
    }
}