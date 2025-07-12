package com.example.resqr.domain.usecase.password

import com.example.resqr.domain.repository.passwordRepository.PasswordRepository

class GetPasswordUseCase(private val passwordRepository: PasswordRepository) {
    operator fun invoke(userId: Int) = passwordRepository.getPassword(userId = userId)
}