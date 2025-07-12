package com.example.resqr.domain.model.medicalRecordModel

sealed class MedicalResponse {
    object Uninitialized : MedicalResponse()
    object Loading : MedicalResponse()
    data class GetMedicalData(val medicalData: UserWithMedicalData?) : MedicalResponse()
    data class MedicalError(val message: String) : MedicalResponse()
    data class MedicalSuccess(val user: String) : MedicalResponse()
}