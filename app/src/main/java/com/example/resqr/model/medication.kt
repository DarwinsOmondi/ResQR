package com.example.resqr.model

import kotlinx.serialization.Serializable

@Serializable
data class medication (
    val name: String,
    val dosage: String,
    val frequency: String,
    val duration: String
)