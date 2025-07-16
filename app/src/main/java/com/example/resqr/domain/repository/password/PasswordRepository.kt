package com.example.resqr.domain.repository.password

import com.example.resqr.domain.model.passwordModel.PasswordResponse
import kotlinx.coroutines.flow.Flow

interface PasswordRepository {
    fun getPassword(userId: Int): Flow<PasswordResponse>
    fun savePassword(userId: Int, password: String, enabled: Boolean): Flow<PasswordResponse>
    fun deletePassword(userId: Int): Flow<PasswordResponse>
    fun updatePassword(userId: Int, newPassword: String, enabled: Boolean): Flow<PasswordResponse>

    fun isPasswordCorrect(userId: Int, password: String, enabled: Boolean): Flow<PasswordResponse>
}