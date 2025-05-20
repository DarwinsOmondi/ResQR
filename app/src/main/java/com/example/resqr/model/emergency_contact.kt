package com.example.resqr.model

import kotlinx.serialization.Serializable

@Serializable
data class Emergency_contact(
    val name: String,
    val phone_number: String,
)