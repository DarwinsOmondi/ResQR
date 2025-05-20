package com.example.resqr.clienthome

import android.graphics.Bitmap
import android.util.Log
import com.example.resqr.model.User
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.Json
import java.util.UUID
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

class ClientRepository(private val supabaseClient: SupabaseClient) {

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
}