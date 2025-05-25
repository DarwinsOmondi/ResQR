package com.example.resqr.model

import kotlinx.serialization.Serializable

@Serializable
data class  Alert(
    val latitude: Double,
    val longitude: Double,
    val resolved: Boolean = false
)