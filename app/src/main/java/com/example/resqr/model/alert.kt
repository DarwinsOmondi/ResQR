package com.example.resqr.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val id: Int = 0,
    @SerialName("alertSentAt")
    val alertSentAt: String,
    val latitude: Double,
    val longitude: Double,
    @SerialName("resolved")
    var resolved: String,
)