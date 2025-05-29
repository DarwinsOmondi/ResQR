package com.example.resqr.model

import kotlinx.serialization.Serializable

@Serializable
data class UserMedicalData(
    val fullName: String? = null,
    val blood_type: String,
    val gender: String,
    val allergies: List<Allergy>,
    val medications: List<Medication>,
    val conditions: String,
    val emergency_contact: List<Emergency_contact>
)