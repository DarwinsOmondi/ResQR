package com.example.resqr.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val filename: String,
    val email: String,
    val phone_number: String,
    val role: String,
    val medicalData: UserMedicalData
)