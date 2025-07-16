package com.example.resqr.domain.usecase.password

import com.example.resqr.domain.repository.password.PasswordRepository

class IsPasswordCorrect(private val passwordRepository: PasswordRepository) {
    operator fun invoke(userId: Int, password: String, enabled: Boolean) =
        passwordRepository.isPasswordCorrect(userId, password, enabled = enabled)
}