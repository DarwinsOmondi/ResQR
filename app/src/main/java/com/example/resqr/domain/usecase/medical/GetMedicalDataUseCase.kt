package com.example.resqr.domain.usecase.medical

import com.example.resqr.domain.repository.medical.MedicalRepository

class GetMedicalDataUseCase(private val medicalRepository: MedicalRepository) {
     operator fun invoke() = medicalRepository.getMedicalData()
}