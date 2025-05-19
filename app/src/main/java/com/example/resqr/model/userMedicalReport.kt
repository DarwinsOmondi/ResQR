package com.example.resqr.model

import kotlinx.serialization.Serializable

@Serializable
data class UserMedicalData(
    val blood_type: String,
    val gender: String,
    val allergies: List<allergy>,
    val medications: List<medication>,
    val conditions: String,
    val emergency_contact: List<emergency_contact>
)