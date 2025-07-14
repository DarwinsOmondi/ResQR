package com.example.resqr.domain.usecase.password

import com.example.resqr.domain.repository.password.PasswordRepository

class UpdatePasswordUseCase(private val passwordRepository: PasswordRepository) {
    operator fun invoke(userId: Int, newPassword: String) =
        passwordRepository.updatePassword(userId = userId, newPassword = newPassword)
}