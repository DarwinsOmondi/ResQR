package com.example.resqr.model

import kotlinx.serialization.Serializable

@Serializable
data class alert(
    val id: String? = null,
    val user_id: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String? = null,
    val resolved: Boolean = false
)