package com.example.resqr.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val fullname: String,
    val email: String,
    val phone_number: String,
    val role: String,
    val medicaldata: String
)