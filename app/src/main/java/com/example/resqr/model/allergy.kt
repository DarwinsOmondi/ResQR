package com.example.resqr.model

import kotlinx.serialization.Serializable

@Serializable
data class allergy(
    val substance: String,
    val reaction: String
)