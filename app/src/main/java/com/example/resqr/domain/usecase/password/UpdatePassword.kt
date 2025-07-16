package com.example.resqr.domain.usecase.password

import com.example.resqr.domain.repository.password.PasswordRepository

class UpdatePassword(private val passwordRepository: PasswordRepository) {
    operator fun invoke(userId: Int, newPassword: String, enabled: Boolean) =
        passwordRepository.updatePassword(
            userId = userId,
            newPassword = newPassword,
            enabled = enabled
        )
}