package com.example.resqr.domain.usecase.password

import com.example.resqr.domain.repository.password.PasswordRepository

class SavePassword(private val passwordRepository: PasswordRepository) {
    operator fun invoke(userId: Int, password: String, enabled: Boolean) =
        passwordRepository.savePassword(
            userId = userId, password = password,
            enabled = enabled
        )
}