package com.example.resqr.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.resqr.data.local.entity.PasswordEntity

@Dao
interface PasswordDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insertPassword(passwordEntity: PasswordEntity)

    @Query("SELECT * FROM password_table WHERE userId = :userId")
    fun getPasswordByUserId(userId: Int): PasswordEntity?

    @Query("DELETE FROM password_table WHERE userId = :userId")
    fun deletePasswordByUserId(userId: Int)

    @Query("UPDATE password_table SET password = :newPassword, enabled = :enabled WHERE userId = :userId")
    fun updatePasswordByUserId(userId: Int, newPassword: String, enabled: Boolean)

    @Query("SELECT COUNT(*) FROM password_table WHERE userId = :userId AND password = :encryptedPassword")
    suspend fun isPasswordCorrect(userId: Int, encryptedPassword: String): Int
}