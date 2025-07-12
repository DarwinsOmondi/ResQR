package com.example.resqr.domain.usecase.password

import com.example.resqr.domain.repository.passwordRepository.PasswordRepository

class SavePasswordUseCase(private val passwordRepository: PasswordRepository) {
    operator fun invoke(userId: Int, password: String) =
        passwordRepository.savePassword(userId = userId, password = password)
}