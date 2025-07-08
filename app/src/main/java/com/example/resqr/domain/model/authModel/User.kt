package com.example.resqr.domain.model.authModel

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = 0,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
)