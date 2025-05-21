package com.example.resqr.clientprofile

import android.util.Log
import com.example.resqr.model.User
import com.example.resqr.model.UserDto
import com.example.resqr.model.UserMedicalData
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.Json
import java.util.UUID

class ClientProfileRepository(private val supabaseClient: SupabaseClient) {

    suspend fun fetchMedicalData(userId: String): Result<User> {
        return try {
            val userDto = supabaseClient.from("clientmedicaldata")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<UserDto>()

            val medicalData = Json.decodeFromString<UserMedicalData>(userDto.medicaldata)

            val user = User(
                fullname = userDto.fullname,
                email = userDto.email,
                phone_number = userDto.phone_number,
                role = userDto.role,
                medicalData = medicalData
            )
            Result.success(user)
        } catch (e: Exception) {
            Log.e("ClientProfileRepository", "Error fetching medical data", e)
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
                    "Bad Request",
                    ignoreCase = true
                ) || message.contains(
                    "invalid input",
                    ignoreCase = true
                ) -> "Invalid data provide.Please check your input."

                message.contains(
                    "Not Found",
                    ignoreCase = true
                ) -> "The requested data was not found."

                message.contains(
                    "does not exist",
                    ignoreCase = true
                ) -> "There is a problem with the database .PLease contact the support."

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

                message.contains(
                    "List is empty",
                    ignoreCase = true
                ) -> "You have no medical data to display"

                else -> "An expected error occurred : ${e.message}"
            }
            Result.failure(Exception(userMessage))
        }
    }

    suspend fun signOutUser(): Result<String> {
        return try {
            supabaseClient.auth.signOut()
            Result.success("Signed out successfully")
        } catch (e: Exception) {
            Log.e("ClientProfileRepository", "Error signing out", e)
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
                ) -> "Network issue .Please try again later."

                message.contains(
                    "Internal Server Error",
                    ignoreCase = true
                ) -> "A server error occurred. Try again later."

                else -> {}
            }
            Result.failure(Exception(userMessage.toString()))
        }
    }
}