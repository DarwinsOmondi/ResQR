package com.example.resqr.domain.usecase.user

import com.example.resqr.domain.repository.userRepository.UserRepository

class DeleteUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(id: Int) = userRepository.deleteUser(id = id)
}