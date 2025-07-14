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
            Log.d("PasswordRepositoryImpl", "Fetched password: ${passwordEntity?.password}")
            if (passwordEntity != null) {
              //  val decryptedPassword = EncryptionUtils.decrypt(passwordEntity.password)
                emit(PasswordResponse.GetPassword(passwordEntity.password))
            } else {
                emit(PasswordResponse.PasswordError("No password found for userId: $userId"))
            }
        } catch (e: Exception) {
            Log.e("PasswordRepositoryImpl", "Failed to fetch password", e)
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to fetch password"))
        }
    }

    override fun savePassword(userId: Int, password: String): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
           // val encrypted = EncryptionUtils.encrypt(password)
            withContext(Dispatchers.IO) {
                passwordDao.insertPassword(PasswordEntity(userId = userId, password = password))
            }
            Log.d("PasswordRepositoryImpl", "Password successfully saved for userId: $userId")
            emit(PasswordResponse.GetPassword(password))
        } catch (e: Exception) {
            Log.e("PasswordRepositoryImpl", "Failed to save password", e)
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to save password"))
        }
    }

    override fun deletePassword(userId: Int): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
            withContext(Dispatchers.IO) {
                passwordDao.deletePasswordByUserId(userId)
            }
            emit(PasswordResponse.GetPassword(""))
        } catch (e: Exception) {
            Log.e("PasswordRepositoryImpl", "Failed to delete password", e)
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to delete password"))
        }
    }

    override fun updatePassword(userId: Int, newPassword: String): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
         //   val encrypted = EncryptionUtils.encrypt(newPassword)
            withContext(Dispatchers.IO) {
                passwordDao.updatePasswordByUserId(userId, newPassword)
            }
            emit(PasswordResponse.GetPassword(newPassword))
        } catch (e: Exception) {
            Log.e("PasswordRepositoryImpl", "Failed to update password", e)
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to update password"))
        }
    }

    override fun isPasswordCorrect(userId: Int, password: String): Flow<PasswordResponse> = flow {
        emit(PasswordResponse.Loading)
        try {
         //   val encryptedPassword = EncryptionUtils.encrypt(password)
            val count = withContext(Dispatchers.IO) {
                passwordDao.isPasswordCorrect(userId, password)
            }
            emit(PasswordResponse.PasswordSuccess(count > 0))
        } catch (e: Exception) {
            Log.e("PasswordRepositoryImpl", "Failed to check password", e)
            emit(PasswordResponse.PasswordError(e.message ?: "Failed to check password"))
        }
    }

}