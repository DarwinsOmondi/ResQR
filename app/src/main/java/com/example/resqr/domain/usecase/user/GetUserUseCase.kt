package com.example.resqr.domain.usecase.user

import com.example.resqr.domain.repository.userRepository.UserRepository

class GetUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.getUser()
}