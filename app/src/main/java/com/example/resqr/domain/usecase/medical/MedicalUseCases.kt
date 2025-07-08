package com.example.resqr.domain.usecase.medical

data class MedicalUseCases(
    val insertMedicalData: InsertMedicalDataUseCase,
    val getMedicalData: GetMedicalDataUseCase,
    val deleteMedicalData: DeleteMedicalDataUseCase,
    val updateMedicalData: UpdateMedicalDataUseCase,
    val getCurrentUserMedicalData: GetCurrentUserMedicalDataUseCase
)
