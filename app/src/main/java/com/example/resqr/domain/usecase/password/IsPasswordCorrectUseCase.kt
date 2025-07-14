package com.example.resqr.domain.usecase.password

import com.example.resqr.domain.repository.password.PasswordRepository

class IsPasswordCorrectUseCase(private val passwordRepository: PasswordRepository) {
    operator fun invoke(userId: Int, password: String) =
        passwordRepository.isPasswordCorrect(userId, password)
}