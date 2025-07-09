package com.example.resqr.domain.model.medicalRecordModel

import com.example.resqr.model.Emergency_contact
import kotlinx.serialization.Serializable

@Serializable
data class UserMedicalData(
    val age: String,
    val bloodType: String,
    val allergies: List<Allergy>,
    val medications: List<Medication>,
    val conditions: List<MedicalConditions>,
    val immunizations: List<Immunizations>,
    val emergencyContact: List<EmergencyContact>
)