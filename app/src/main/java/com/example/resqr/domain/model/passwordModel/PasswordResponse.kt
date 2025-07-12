package com.example.resqr.domain.model.passwordModel

sealed class PasswordResponse {
    object Uninitialized : PasswordResponse()
    object Loading : PasswordResponse()
    data class GetPassword(val password: String) : PasswordResponse()
    data class PasswordError(val message: String) : PasswordResponse()
    data class PasswordSuccess(val isCorrect: Boolean) : PasswordResponse()

}