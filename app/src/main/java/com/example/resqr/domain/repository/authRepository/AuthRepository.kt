package com.example.resqr.domain.repository.authRepository

import com.example.resqr.domain.model.authModel.AuthResponse

interface AuthRepository {
    suspend fun signUp(email: String, password: String, phoneNumber: Int, fullName: String): AuthResponse
    suspend fun signIn(email: String, password: String): AuthResponse
    suspend fun getCurrentUser(): AuthResponse
}