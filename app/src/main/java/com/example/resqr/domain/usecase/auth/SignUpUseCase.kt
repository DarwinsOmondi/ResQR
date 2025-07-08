package com.example.resqr.domain.usecase.authUseCase

import com.example.resqr.domain.repository.authRepository.AuthRepository
import com.example.resqr.signup.SignUpViewmodel

class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, role: String) =
        authRepository.signUp(email = email, password = password, role = role)
}