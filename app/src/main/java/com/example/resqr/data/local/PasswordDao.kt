package com.example.resqr.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PasswordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPassword(passwordEntity: PasswordEntity)

    @Query("SELECT * FROM password_table WHERE userId = :userId")
    fun getPasswordByUserId(userId: Int): PasswordEntity?

    @Query("DELETE FROM password_table WHERE userId = :userId")
    fun deletePasswordByUserId(userId: Int)

    @Query("UPDATE password_table SET password = :newPassword WHERE userId = :userId")
    fun updatePasswordByUserId(userId: Int, newPassword: String)

    @Query("SELECT COUNT(*) FROM password_table WHERE userId = :userId AND password = :encryptedPassword")
    suspend fun isPasswordCorrect(userId: Int, encryptedPassword: String):Int
}