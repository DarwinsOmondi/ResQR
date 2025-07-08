package com.example.resqr.domain.usecase.medical

import com.example.resqr.domain.repository.medicalRepository.MedicalRepository

class GetMedicalDataUseCase(private val medicalRepository: MedicalRepository) {
     operator fun invoke() = medicalRepository.getMedicalData()
}