package com.example.resqr.data.repositoryImpl

import android.util.Log
import com.example.resqr.data.local.dao.PasswordDao
import com.example.resqr.data.local.entity.PasswordEntity
import com.example.resqr.domain.model.passwordModel.PasswordResponse
import com.example.resqr.domain.repository.password.PasswordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PasswordRepositoryImpl(private val passwordDao: PasswordDao) : PasswordRepository {

    override fun getPassword(userId: Int): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            val passwordEntity = withContext(Dispatchers.IO) {
                passwordDao.getPasswordByUserId(userId)
            }
            if (passwordEntity != null) {
                emit(PasswordResponse.GetPassword(passwordEntity))
            } else {
                emit(PasswordResponse.PasswordError(""))
            }
            Log.d("PasswordRepositoryImpl", "Fetched password: $passwordEntity")
        } catch (e: Exception) {
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to fetch password"))
        }
    }

    override fun savePassword(
        userId: Int,
        password: String,
        enabled: Boolean
    ): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            val entity = PasswordEntity(userId = userId, password = password, enabled = enabled)
            withContext(Dispatchers.IO) {
                passwordDao.insertPassword(entity)
            }
            emit(PasswordResponse.GetPassword(entity))
        } catch (e: Exception) {
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to save password"))
        }
    }

    override fun updatePassword(
        userId: Int,
        newPassword: String,
        enabled: Boolean
    ): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            withContext(Dispatchers.IO) {
                passwordDao.updatePasswordByUserId(userId, newPassword, enabled)
            }
            val updatedPassword = withContext(Dispatchers.IO) {
                passwordDao.getPasswordByUserId(userId)
            }
            if (updatedPassword != null) {
                emit(PasswordResponse.GetPassword(updatedPassword))
            } else {
                emit(PasswordResponse.PasswordError("Failed to retrieve updated password"))
            }
        } catch (e: Exception) {
            emit(PasswordResponse.PasswordError("Update failed: ${e.message}"))
        }
    }


    override fun deletePassword(userId: Int): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            withContext(Dispatchers.IO) {
                passwordDao.deletePasswordByUserId(userId)
            }
            emit(
                PasswordResponse.GetPassword(
                    PasswordEntity(
                        userId = userId,
                        password = "",
                        enabled = false
                    )
                )
            )
        } catch (e: Exception) {
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to delete password"))
        }
    }


    override fun isPasswordCorrect(
        userId: Int,
        password: String,
        enabled: Boolean
    ): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            //   val encryptedPassword = EncryptionUtils.encrypt(password)
            val count = withContext(Dispatchers.IO) {
                passwordDao.isPasswordCorrect(userId, password)
            }
            emit(PasswordResponse.PasswordSuccess(count > 0))
        } catch (e: Exception) {
            Log.e("PasswordRepositoryImpl", "Failed to check password", e)
            emit(PasswordResponse.PasswordError("Incorrect password"))
        }
    }

}