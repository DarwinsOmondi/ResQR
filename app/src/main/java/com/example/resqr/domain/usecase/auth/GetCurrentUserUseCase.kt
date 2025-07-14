package com.example.resqr.domain.usecase.auth

import com.example.resqr.domain.repository.auth.AuthRepository

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.getCurrentUser()
}