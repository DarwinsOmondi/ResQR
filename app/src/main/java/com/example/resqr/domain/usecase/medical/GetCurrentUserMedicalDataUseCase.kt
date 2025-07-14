package com.example.resqr.domain.usecase.medical

import com.example.resqr.domain.repository.medical.MedicalRepository

class GetCurrentUserMedicalDataUseCase(private val medicalRepository: MedicalRepository) {
    operator fun invoke(id: Int) = medicalRepository.getCurrentUserMedicalData(id = id)
}