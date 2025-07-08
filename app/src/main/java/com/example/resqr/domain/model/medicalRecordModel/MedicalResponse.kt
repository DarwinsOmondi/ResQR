package com.example.resqr.domain.model.medicalRecordModel

sealed class MedicalResults {
    object Uninitialized : MedicalResults()
    object Loading : MedicalResults()
    data class GetMedicalData(val medicalData: UserMedicalData?) : MedicalResults()
    data class MedicalError(val message: String) : MedicalResults()
}