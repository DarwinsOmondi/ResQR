package com.example.resqr.domain.repository.userRepository

import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.model.usermodel.UserResponse
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    fun insertUser(user: User): Flow<UserResponse>
    fun getUser(): Flow<UserResponse>
    fun updateUser(user: User): Flow<UserResponse>
    fun deleteUser(id: Int): Flow<UserResponse>
}