package com.example.resqr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.domain.model.authModel.AuthResponse
import com.example.resqr.domain.usecase.auth.GetCurrentUserUseCase
import com.example.resqr.domain.usecase.auth.SignInUseCase
import com.example.resqr.domain.usecase.auth.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {


    private val _authState = MutableStateFlow<AuthResponse>(AuthResponse.Uninitialized)
    val authState: MutableStateFlow<AuthResponse> = _authState

    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex: StateFlow<Int> = _selectedIndex
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _passwordConfirmation = MutableStateFlow("")
    val passwordConfirmation: StateFlow<String> = _passwordConfirmation
    private val _passwordVisibility = MutableStateFlow(false)
    val passwordVisibility: StateFlow<Boolean> = _passwordVisibility

    fun onSelectedIndexChanged(index: Int) {
        _selectedIndex.value = index
    }

    val onPasswordChange: (String) -> Unit = { newPassword ->
        _password.value = newPassword
    }

    val onPhoneNumberChange: (String) -> Unit = { newPhoneNumber ->
        _phoneNumber.value = newPhoneNumber
    }
    val onFullNameChange: (String) -> Unit = { newFullName ->
        _fullName.value = newFullName
    }
    val onEmailChange: (String) -> Unit = { newEmail ->
        _email.value = newEmail
    }
    val onPasswordConfirmationChange: (String) -> Unit = { newPasswordConfirmation ->
        _passwordConfirmation.value = newPasswordConfirmation
    }
    
    fun togglePasswordVisibility() {
        _passwordVisibility.value = !_passwordVisibility.value
    }


    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthResponse.Loading
            val result = signInUseCase(email, password)
            _authState.value = result
        }
    }

    fun signUp(email: String, password: String, phoneNumber: Int, fullName: String) {
        viewModelScope.launch {
            _authState.value = AuthResponse.Loading
            val result = signUpUseCase(email, password, phoneNumber, fullName)
            _authState.value = result
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _authState.value = AuthResponse.Loading
            val result = getCurrentUserUseCase()
            _authState.value = when (result) {
                is AuthResponse.GetAuthUser -> result
                is AuthResponse.AuthError -> result
                else -> AuthResponse.AuthError("Unexpected error")
            }
        }
    }
}