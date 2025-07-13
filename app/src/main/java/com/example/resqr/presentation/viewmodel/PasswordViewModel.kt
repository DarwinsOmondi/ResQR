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

    private val _confirmPassword = MutableStateFlow<String?>(null)
    val confirmPassword: StateFlow<String?> = _confirmPassword

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    private val _isConfirmPasswordVisible = MutableStateFlow(false)
    val isConfirmPasswordVisible: StateFlow<Boolean> = _isConfirmPasswordVisible

    private val _isPasswordAvailable = MutableStateFlow(false)
    val isPasswordAvailable: StateFlow<Boolean> = _isPasswordAvailable

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun onPasswordVisibilityChanged() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun onConfirmPasswordVisibilityChanged() {
        _isConfirmPasswordVisible.value = !_isConfirmPasswordVisible.value
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

                if (result is PasswordResponse.GetPassword && result.password.isNotEmpty()) {
                    populateIsPasswordAvailable(result.password)
                }
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

    fun checkPasswordSimilarity(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun populateIsPasswordAvailable(password: String) {
        _isPasswordAvailable.value = password.isNotEmpty()
    }

    fun resetTextFields() {
        _password.value = ""
        _confirmPassword.value = ""

    }
}