package com.example.resqr.domain.usecase.auth

import com.example.resqr.domain.repository.auth.AuthRepository

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signIn(email = email, password = password)
}