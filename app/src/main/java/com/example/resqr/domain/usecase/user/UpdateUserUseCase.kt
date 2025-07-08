package com.example.resqr.domain.usecase.user

import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.repository.userRepository.UserRepository

class UpdateUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(user: User) = userRepository.updateUser(user = user)
}