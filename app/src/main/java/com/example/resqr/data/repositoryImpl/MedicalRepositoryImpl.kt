package com.example.resqr.data.repositoryImpl

import android.util.Log
import com.example.resqr.domain.model.medicalRecordModel.MedicalResponse
import com.example.resqr.domain.model.medicalRecordModel.UserWithMedicalData
import com.example.resqr.domain.repository.medical.MedicalRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MedicalRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : MedicalRepository {

    override fun insertMedicalData(userWithMedicalData: UserWithMedicalData): Flow<MedicalResponse> =
        flow {
            emit(MedicalResponse.Loading)
            try {
                supabaseClient.postgrest["userMedicalData"].insert(userWithMedicalData)
                emit(MedicalResponse.GetMedicalData(userWithMedicalData))
                emit(MedicalResponse.MedicalSuccess("Medical data added successfully"))
            } catch (e: Exception) {
                Log.e("MedicalRepositoryImpl", "Error inserting medical data", e)
                emit(MedicalResponse.MedicalError(userFriendlyErrorMessage(e.message)))
            }
        }

    override fun getMedicalData(): Flow<MedicalResponse> = flow {
        emit(MedicalResponse.Loading)
        try {
            val result = supabaseClient.postgrest["userMedicalData"].select()
                .decodeList<UserWithMedicalData>()
            emit(MedicalResponse.GetMedicalData(result.firstOrNull()))
        } catch (e: Exception) {
            Log.e("MedicalRepositoryImpl", "Error getting medical data", e)
            emit(MedicalResponse.MedicalError(userFriendlyErrorMessage(e.message)))
        }
    }

    override fun updateMedicalData(userWithMedicalData: UserWithMedicalData): Flow<MedicalResponse> =
        flow {
            emit(MedicalResponse.Loading)
            try {
                supabaseClient.postgrest["userMedicalData"]
                    .update(userWithMedicalData) {
                        filter {
                            eq("id", userWithMedicalData.id)
                        }
                    }
                emit(MedicalResponse.MedicalSuccess("Medical data updated successfully"))
                emit(MedicalResponse.GetMedicalData(userWithMedicalData))
            } catch (e: Exception) {
                Log.e("MedicalRepositoryImpl", "Error updating medical data", e)
                emit(MedicalResponse.MedicalError(userFriendlyErrorMessage(e.message)))
            }
        }

    override fun deleteMedicalData(id: String): Flow<MedicalResponse> = flow {
        emit(MedicalResponse.Loading)
        try {
            supabaseClient.postgrest["userMedicalData"].delete {
                filter {
                    eq("userId", id)
                }
            }
            emit(MedicalResponse.MedicalSuccess("Medical data deleted successfully"))
            emit(MedicalResponse.GetMedicalData(null))
        } catch (e: Exception) {
            Log.e("MedicalRepositoryImpl", "Error deleting medical data", e)
            emit(MedicalResponse.MedicalError(userFriendlyErrorMessage(e.message)))
        }
    }

    override fun getCurrentUserMedicalData(id: Int): Flow<MedicalResponse> = flow {
        emit(MedicalResponse.Loading)
        try {
            val result = supabaseClient.postgrest["userMedicalData"]
                .select {
                    filter {
                        eq("userId", id)
                    }
                }.decodeSingle<UserWithMedicalData>()
            emit(MedicalResponse.GetMedicalData(result))
        } catch (e: Exception) {
            Log.e("MedicalRepositoryImpl", "Error getting current user medical data", e)
            emit(MedicalResponse.MedicalError(userFriendlyErrorMessage(e.message)))
        }
    }
}
