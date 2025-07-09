package com.example.resqr.domain.model.alertModel

import com.example.resqr.domain.model.medicalRecordModel.EmergencyContact
import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val id: Int = 0,
    val userId: Int,
    val victimName: String,
    val victimPhoneNumber: String,
    val victimLatitude: String,
    val victimLongitude: String,
    val victimEmergencyContact: EmergencyContact?,
    val isActive: Boolean = true,
    val alertType: AlertType = AlertType.GENERAL
)
