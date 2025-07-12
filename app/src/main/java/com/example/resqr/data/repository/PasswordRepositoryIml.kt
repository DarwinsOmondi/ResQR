package com.example.resqr.data.repository

import com.example.resqr.data.local.PasswordDao
import com.example.resqr.data.local.PasswordEntity
import com.example.resqr.domain.model.passwordModel.PasswordResponse
import com.example.resqr.domain.repository.passwordRepository.PasswordRepository
import com.example.resqr.utils.EncryptionUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PasswordRepositoryImpl(private val passwordDao: PasswordDao) : PasswordRepository {

    override fun getPassword(userId: Int): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            val passwordEntity = passwordDao.getPasswordByUserId(userId)
            if (passwordEntity != null) {
                val decryptedPassword = EncryptionUtils.decrypt(passwordEntity.password)
                emit(PasswordResponse.GetPassword(decryptedPassword))
            } else {
                emit(PasswordResponse.PasswordError("No password found for userId: $userId"))
            }
        } catch (e: Exception) {
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to fetch password"))
        }
    }


    override fun savePassword(userId: Int, password: String): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            val encrypted = EncryptionUtils.encrypt(password)
            passwordDao.insertPassword(PasswordEntity(userId = userId, password = encrypted))
            emit(PasswordResponse.GetPassword(password))
        } catch (e: Exception) {
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to save password"))
        }
    }


    override fun deletePassword(userId: Int): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            passwordDao.deletePasswordByUserId(userId)
            emit(PasswordResponse.GetPassword(""))
        } catch (e: Exception) {
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to delete password"))
        }
    }

    override fun updatePassword(userId: Int, newPassword: String): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            val encrypted = EncryptionUtils.encrypt(newPassword)
            passwordDao.updatePasswordByUserId(userId, encrypted)
            emit(PasswordResponse.GetPassword(newPassword))
        } catch (e: Exception) {
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to update password"))
        }
    }

    override fun isPasswordCorrect(userId: Int, password: String): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            val encryptedPassword = EncryptionUtils.encrypt(password)
            val count = passwordDao.isPasswordCorrect(userId, encryptedPassword)
            val isCorrect = count > 0
            emit(PasswordResponse.PasswordSuccess(isCorrect))
        } catch (e: Exception) {
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to check password"))
        }
    }
}