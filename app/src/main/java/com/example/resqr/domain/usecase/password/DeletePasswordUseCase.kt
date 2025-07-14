package com.example.resqr.domain.usecase.password

import com.example.resqr.domain.repository.password.PasswordRepository

class DeletePasswordUseCase(private val passwordRepository: PasswordRepository) {
    operator fun invoke(userId: Int) = passwordRepository.deletePassword(userId = userId)
}