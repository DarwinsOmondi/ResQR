package com.example.resqr.domain.model.medicalRecordModel

import kotlinx.serialization.Serializable

@Serializable
data class EmergencyContact(
    val name: String,
    val phoneNumber: String,
)