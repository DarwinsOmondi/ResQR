package com.example.resqr.domain.model.authModel

sealed class AuthResponse {
    object Uninitialized : AuthResponse()
    object Loading : AuthResponse()
    data class GetAuthUser(val user: User?) : AuthResponse()
    data class AuthError(val message: String) : AuthResponse()
}