package com.example.resqr.data.repository

import android.util.Log
import com.example.resqr.domain.model.alertModel.Alert
import com.example.resqr.domain.model.alertModel.AlertResponse
import com.example.resqr.domain.repository.alertRepository.AlertRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AlertRepositoryImpl(private val supabaseClient: SupabaseClient) : AlertRepository {

    override fun sendAlert(alert: Alert): Flow<AlertResponse> = flow {
        emit(AlertResponse.Loading)
        try {
            supabaseClient.postgrest["alerts"].insert(alert)
            emit(AlertResponse.GetAlert(alert))
            emit(AlertResponse.Success("Alert sent successfully"))
        } catch (e: Exception) {
            Log.e("AlertRepositoryImpl", "Error sending alert: ${e.message}")
            emit(AlertResponse.AlertError(userFriendlyErrorMessage(e.message)))
        }
    }

    override fun getAlerts(): Flow<AlertResponse> = flow {
        emit(AlertResponse.Loading)
        try {
            val alerts = supabaseClient.postgrest["alerts"].select().decodeList<Alert>()
            emit(AlertResponse.GetAlert(alerts.firstOrNull()))
        } catch (e: Exception) {
            Log.e("AlertRepositoryImpl", "Error getting alerts: ${e.message}")
            emit(AlertResponse.AlertError(userFriendlyErrorMessage(e.message)))
        }
    }

    override fun updateAlert(alert: Alert): Flow<AlertResponse> = flow {
        emit(AlertResponse.Loading)
        try {
            supabaseClient.postgrest["alerts"].update(alert) {
                filter {
                    eq("id", alert.id)
                }
            }
            emit(AlertResponse.GetAlert(alert))
        } catch (e: Exception) {
            Log.e("AlertRepositoryImpl", "Error updating alert: ${e.message}")
            emit(AlertResponse.AlertError(userFriendlyErrorMessage(e.message)))
        }
    }
}