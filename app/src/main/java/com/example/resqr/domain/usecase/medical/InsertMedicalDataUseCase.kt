package com.example.resqr.domain.usecase.medical

import com.example.resqr.domain.model.medicalRecordModel.UserWithMedicalData
import com.example.resqr.domain.repository.medicalRepository.MedicalRepository

class InsertMedicalDataUseCase(private val medicalRepository: MedicalRepository) {
     operator fun invoke(userWithMedicalData: UserWithMedicalData) =
        medicalRepository.insertMedicalData(userWithMedicalData)
}