package com.example.resqr.domain.model.passwordModel

import com.example.resqr.data.local.entity.PasswordEntity

sealed class PasswordResponse {
    object Uninitialized : PasswordResponse()
    object Loading : PasswordResponse()
    data class GetPassword(val password: PasswordEntity) : PasswordResponse()
    data class PasswordError(val message: String) : PasswordResponse()
    data class PasswordSuccess(val isCorrect: Boolean) : PasswordResponse()
}