package com.example.resqr.domain.repository.passwordRepository

import com.example.resqr.domain.model.passwordModel.PasswordResponse
import kotlinx.coroutines.flow.Flow

interface PasswordRepository {
    fun getPassword(userId: Int): Flow<PasswordResponse>
    fun savePassword(userId: Int, password: String): Flow<PasswordResponse>
    fun deletePassword(userId: Int): Flow<PasswordResponse>
    fun updatePassword(userId: Int, newPassword: String): Flow<PasswordResponse>

    fun isPasswordCorrect(userId: Int, password: String): Flow<PasswordResponse>

}