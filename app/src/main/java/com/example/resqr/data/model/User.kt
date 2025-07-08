package com.example.resqr.data.model

import com.example.resqr.model.UserMedicalData
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = 0,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
)
