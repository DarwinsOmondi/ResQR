package com.example.resqr.domain.usecase.authUseCase

import com.example.resqr.domain.repository.authRepository.AuthRepository

class SignInCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signIn(email = email, password = password)
}