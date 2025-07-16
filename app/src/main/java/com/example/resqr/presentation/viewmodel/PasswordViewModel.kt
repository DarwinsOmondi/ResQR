package com.example.resqr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.domain.model.passwordModel.PasswordResponse
import com.example.resqr.domain.usecase.password.Password
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PasswordViewModel(private val passwordUseCase: Password) : ViewModel() {
    private val _passwordState = MutableStateFlow<PasswordResponse>(PasswordResponse.Uninitialized)
    val passwordState: StateFlow<PasswordResponse> = _passwordState

    private val _password = MutableStateFlow<String?>(null)
    val password: StateFlow<String?> = _password

    private val _unLockPassword = MutableStateFlow<String?>(null)
    val unLockPassword: StateFlow<String?> = _unLockPassword

    private val _backGroundPassword = MutableStateFlow<String?>(null)
    val backGroundPassword: StateFlow<String?> = _backGroundPassword

    private val _confirmPassword = MutableStateFlow<String?>(null)
    val confirmPassword: StateFlow<String?> = _confirmPassword

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    private val _isConfirmPasswordVisible = MutableStateFlow(false)
    val isConfirmPasswordVisible: StateFlow<Boolean> = _isConfirmPasswordVisible

    private val _isPasswordAvailable = MutableStateFlow(false)
    val isPasswordAvailable: StateFlow<Boolean> = _isPasswordAvailable

    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked


    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value

    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun onUnlockPasswordChanged(password: String) {
        _unLockPassword.value = password
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


    fun setPassword(userId: Int, password: String, enabled: Boolean) {
        viewModelScope.launch {
            _passwordState.value = PasswordResponse.Loading
            passwordUseCase.savePassword(
                userId = userId,
                password = password,
                enabled = enabled
            ).collect { result ->
                _passwordState.value = result
            }
        }
        resetTextFields()
    }

    fun getPassword(userId: Int) {
        viewModelScope.launch {
            _passwordState.value = PasswordResponse.Loading
            passwordUseCase.getPassword(userId).collect { result ->
                _passwordState.value = result
                if (result is PasswordResponse.GetPassword && result.password.password.isNotEmpty()) {
                    _backGroundPassword.value = result.password.password
                    populateIsPasswordAvailable(result.password.enabled)
                }
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

    fun updatePassword(userId: Int, password: String, enabled: Boolean) {
        viewModelScope.launch {
            _passwordState.value = PasswordResponse.Loading
            passwordUseCase.updatePassword(userId, password, enabled).collect { result ->
                _passwordState.value = result
            }
            resetTextFields()
        }
    }


    fun isPasswordCorrect(userId: Int, password: String, disabled: Boolean) {
        viewModelScope.launch {
            _passwordState.value = PasswordResponse.Loading
            passwordUseCase.isPasswordCorrect(userId, password, disabled).collect { result ->
                _passwordState.value = result
                if (result is PasswordResponse.PasswordSuccess && result.isCorrect) {
                    _isUnlocked.value = true
                    _password.value = ""
                }
            }
            resetTextFields()
        }
    }

    fun resetUnlockState() {
        _isUnlocked.value = false
    }

    fun checkPasswordSimilarity(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun populateIsPasswordAvailable(enabled: Boolean) {
        _isPasswordAvailable.value = enabled
    }

    fun resetTextFields() {
        onPasswordChanged("")
        onConfirmPasswordChanged("")

    }
}