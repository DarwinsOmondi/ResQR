package com.example.resqr.domain.usecase.password

import com.example.resqr.domain.repository.password.PasswordRepository

class DeletePassword(private val passwordRepository: PasswordRepository) {
    operator fun invoke(userId: Int) = passwordRepository.deletePassword(userId = userId)
}