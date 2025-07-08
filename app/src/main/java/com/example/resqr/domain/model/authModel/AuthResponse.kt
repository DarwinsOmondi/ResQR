package com.example.resqr.domain.model.authModel

sealed class AuthResults {
    object Uninitialized : AuthResults()
    object Loading : AuthResults()
    data class GetAuthUser(val user: User?) : AuthResults()
    data class AuthError(val message: String) : AuthResults()
}