package com.example.resqr.domain.usecase.medical

import com.example.resqr.domain.model.medicalRecordModel.UserWithMedicalData
import com.example.resqr.domain.repository.medical.MedicalRepository

class UpdateMedicalDataUseCase(private val medicalRepository: MedicalRepository) {
    operator fun invoke(userWithMedicalData: UserWithMedicalData) =
        medicalRepository.updateMedicalData(userWithMedicalData)
}