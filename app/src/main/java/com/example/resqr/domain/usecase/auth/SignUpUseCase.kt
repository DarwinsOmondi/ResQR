package com.example.resqr.domain.usecase.auth

import com.example.resqr.domain.repository.auth.AuthRepository

class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, phoneNumber: Int, fullName: String) =
        authRepository.signUp(email = email, password = password, phoneNumber = phoneNumber, fullName = fullName)
}