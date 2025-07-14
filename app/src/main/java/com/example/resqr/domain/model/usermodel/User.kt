package com.example.resqr.domain.model.usermodel

data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val description: String
)
