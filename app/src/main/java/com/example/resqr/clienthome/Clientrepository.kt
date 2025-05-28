package com.example.resqr.clienthome

import android.os.CountDownTimer
import android.util.Log
import com.example.resqr.model.Alert
import com.example.resqr.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.Json

class ClientRepository(private val supabaseClient: SupabaseClient) {
    private var countdownTimer: CountDownTimer? = null

    suspend fun saveMedicalData(user: User): Result<String> {
        return try {
            val userMap = mapOf(
                "fullname" to user.fullname,
                "email" to user.email,
                "phone_number" to user.phone_number,
                "role" to user.role,
                "medicaldata" to Json.encodeToString(user.medicalData)
            )
            supabaseClient.postgrest["clientmedicaldata"].insert(userMap)
            Result.success("Your Medical data saved successfully")
        } catch (e: Exception) {
            Log.e("ClientRepository", "Error saving medical data", e)
            val message = e.message ?: ""
            val userMessage = when {
                message.contains(
                    "Unauthorized",
                    ignoreCase = true
                ) -> "You are not logged in. PLease sign in."

                message.contains(
                    "Forbidden",
                    ignoreCase = true
                ) -> "You don't have permission to perform this action."

                message.contains(
                    "timeout",
                    ignoreCase = true
                ) || message.contains(
                    "unreachable",
                    ignoreCase = true
                ) -> "Network issue .Please try again later."

                message.contains(
                    "Internal Server Error",
                    ignoreCase = true
                ) -> "A server error occurred. Try again later."

                else -> "An expected error occurred : ${e.message}"
            }
            Result.failure(Exception("Error saving medical data due to $userMessage"))
        }
    }

    fun alertCountDown(
        totalTime: Int = 5,
        onTick: (Int) -> Unit,
        onFinish: () -> Unit
    ) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(totalTime * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                onTick((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                onFinish()
            }
        }.start()
    }

    fun stopCountdown() {
        countdownTimer?.cancel()
        countdownTimer = null
    }


    suspend fun sendEmergencyAlert(alert: Alert): Result<String> {
        return try {
            // Configure Json to include default values
            val json = Json { encodeDefaults = true }

            // Log the serialized JSON for debugging
            val jsonString = json.encodeToString(Alert.serializer(), alert)
            Log.d("ClientRepository", "Serialized Alert JSON: $jsonString")

            // Insert into Supabase
            supabaseClient.postgrest["alerts"].insert(alert)
            Result.success("Alert sent successfully")
        } catch (e: Exception) {
            Log.e("ClientRepository", "Error sending emergency", e)
            val message = e.message ?: ""
            val userMessage = when {
                message.contains(
                    "Unauthorized",
                    ignoreCase = true
                ) -> "You are not logged in. PLease sign in."

                message.contains(
                    "Forbidden",
                    ignoreCase = true
                ) -> "You don't have permission to perform this action."

                message.contains(
                    "timeout",
                    ignoreCase = true
                ) || message.contains(
                    "unreachable",
                    ignoreCase = true
                ) -> "Network issue .Please try again later."

                message.contains(
                    "Internal Server Error",
                    ignoreCase = true
                ) -> "A server error occurred. Try again later."

                else -> "An expected error occurred : ${e.message}"
            }
            Result.failure(Exception("Error sending an alert due to $userMessage"))
        }
    }

    suspend fun fetchAlertData(userid: String): Result<Alert> {
        return try {
            val alert = supabaseClient.postgrest["alerts"].select() {
                filter {
                    eq("userid", userid)
                }
            }.decodeSingle<Alert>()
            Log.e("FetchAlertData", alert.resolved.toString())
            Log.e("FetchAlertDataDetails", alert.toString())

            Result.success(alert)
        } catch (e: Exception) {
            Log.e("FetchAlertDataDetails", e.message.toString())
            val message = e.message ?: ""
            val userMessage = when {
                message.contains(
                    "Unauthorized",
                    ignoreCase = true
                ) -> "You are not logged in. PLease sign in."

                message.contains(
                    "Forbidden",
                    ignoreCase = true
                ) -> "You don't have permission to perform this action."

                message.contains(
                    "timeout",
                    ignoreCase = true
                ) || message.contains(
                    "unreachable",
                    ignoreCase = true
                ) -> "Network issue .Please try again later."

                message.contains(
                    "Internal Server Error",
                    ignoreCase = true
                ) -> "A server error occurred. Try again later."

                else -> "An expected error occurred : ${e.message}"

            }
            Result.failure(Exception(userMessage))
        }
    }
}