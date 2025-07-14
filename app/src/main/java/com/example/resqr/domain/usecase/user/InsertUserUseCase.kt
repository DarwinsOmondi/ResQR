package com.example.resqr.domain.usecase.user

import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.repository.user.UserRepository

class InsertUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(user: User) = userRepository.insertUser(user = user)
}