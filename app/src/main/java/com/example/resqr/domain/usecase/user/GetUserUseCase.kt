package com.example.resqr.domain.usecase.user

import com.example.resqr.domain.repository.user.UserRepository

class GetUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.getUser()
}