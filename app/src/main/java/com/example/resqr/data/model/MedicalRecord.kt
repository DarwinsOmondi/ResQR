package com.example.resqr.data.model

import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.model.medicalRecordModel.UserMedicalData
import com.example.resqr.domain.model.medicalRecordModel.UserWithMedicalData
import kotlinx.serialization.Serializable

@Serializable
data class UserMedicalRecord(
    val user: User,
    val medicalData: UserMedicalData
) {
    fun toUserMedicalData() = UserWithMedicalData(
        user = user,
        userId = user.id,
        medicalData = medicalData
    )
}

fun UserWithMedicalData.toUserMedicalRecord() = UserMedicalRecord(
    user = user,
    medicalData = medicalData
)