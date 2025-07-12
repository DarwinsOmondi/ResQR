package com.example.resqr.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_table")
data class PasswordEntity(
    @PrimaryKey val userId: Int,
    val password: String
)
