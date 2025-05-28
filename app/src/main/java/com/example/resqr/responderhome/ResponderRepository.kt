package com.example.resqr.responderhome

import android.util.Log
import com.example.resqr.model.Alert
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class ResponderRepository(private val supabaseClient: SupabaseClient) {

    suspend fun fetchAllAlerts(isResolved: String = "false"): Result<List<Alert>> {
        return try {
            val alerts = supabaseClient.postgrest.from("alerts").select() {
                filter {
                    eq("resolved", isResolved)
                }
            }.decodeList<Alert>()
            Log.d("FetchAlertDataResponder", "Fetched ${alerts.size} alerts: $alerts")
            Result.success(alerts)
        } catch (e: Exception) {
            Log.e("FetchAlertDataResponder", "Error fetching alerts", e)
            val message = e.message ?: ""
            val userMessage = when {
                message.contains(
                    "Unauthorized",
                    ignoreCase = true
                ) -> "You are not logged in. Please sign in."

                message.contains(
                    "Forbidden",
                    ignoreCase = true
                ) -> "You don't have permission to perform this action."

                message.contains("timeout", ignoreCase = true) || message.contains(
                    "unreachable",
                    ignoreCase = true
                ) -> "Network issue. Please try again later."

                message.contains(
                    "Internal Server Error",
                    ignoreCase = true
                ) -> "A server error occurred. Try again later."

                else -> "An unexpected error occurred: ${e.message}"
            }
            Result.failure(Exception(userMessage))
        }
    }

    suspend fun respondToAlert(alertId: Int): Result<String> {
        return try {
            supabaseClient.postgrest["alerts"].update(
                {
                    set("resolved", "true")
                }
            ) {
                filter {
                    eq("id", alertId)
                }
            }
            Result.success("Alert resolved successfully")
        } catch (e: Exception) {
            Log.e("ResponderRepository", "Error resolving alert", e)
            val message = e.message ?: ""
            val userMessage = when {
                message.contains(
                    "Unauthorized",
                    ignoreCase = true
                ) -> "You are not logged in. Please sign in."

                message.contains(
                    "Forbidden",
                    ignoreCase = true
                ) -> "You don't have permission to perform this action."

                message.contains("timeout", ignoreCase = true) || message.contains(
                    "unreachable",
                    ignoreCase = true
                ) -> "Network issue. Please try again later."

                message.contains(
                    "Internal Server Error",
                    ignoreCase = true
                ) -> "A server error occurred. Try again later."

                else -> "An unexpected error occurred: ${e.message}"
            }
            Result.failure(Exception("Error resolving alert due to $userMessage"))
        }
    }
}