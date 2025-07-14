package com.example.resqr.domain.repository.medical

import com.example.resqr.domain.model.medicalRecordModel.MedicalResponse
import com.example.resqr.domain.model.medicalRecordModel.UserWithMedicalData
import kotlinx.coroutines.flow.Flow

interface MedicalRepository {
    fun insertMedicalData(userWithMedicalData: UserWithMedicalData): Flow<MedicalResponse>
    fun getMedicalData(): Flow<MedicalResponse>
    fun updateMedicalData(userWithMedicalData: UserWithMedicalData): Flow<MedicalResponse>
    fun deleteMedicalData(id: String): Flow<MedicalResponse>
    fun getCurrentUserMedicalData(id: Int): Flow<MedicalResponse>
}