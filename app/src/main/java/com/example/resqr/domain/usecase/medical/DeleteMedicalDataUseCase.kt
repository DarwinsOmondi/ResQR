package com.example.resqr.domain.usecase.medical

import com.example.resqr.domain.repository.medicalRepository.MedicalRepository

class DeleteMedicalDataUseCase(private val medicalRepository: MedicalRepository) {
     operator fun invoke(id: String) = medicalRepository.deleteMedicalData(id = id)
}