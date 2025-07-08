package com.example.resqr.domain.model.medicalRecordModel

import com.example.resqr.domain.model.authModel.User
import kotlinx.serialization.Serializable

@Serializable
data class UserWithMedicalData(
    val id: Int = 0,
    val userId: Int,
    val user: User,
    val medicalData: UserMedicalData
)