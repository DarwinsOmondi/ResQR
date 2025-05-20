package com.example.resqr.model

import kotlinx.serialization.Serializable

@Serializable
data class Allergy(
    val substance: String,
    val reaction: String
)