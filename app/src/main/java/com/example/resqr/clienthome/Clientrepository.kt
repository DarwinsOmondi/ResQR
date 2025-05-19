package com.example.resqr.clienthome

import com.example.resqr.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import java.util.UUID

class ClientRepository(private val supabaseClient: SupabaseClient) {

    suspend fun saveMedicalData(user: User): Result<String> {
        return try {
            supabaseClient.postgrest["clientmedicaldata"].insert(user)
            Result.success("Your Medical data saved successfully")
        } catch (e: Exception) {
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

    suspend fun fetchMedicalData(userId: UUID): Result<User> {
        return try {
            val response =
                supabaseClient.from("medicaldata").select {
                    filter {
                        eq("userId", userId)
                    }
                }
                    .decodeSingle<User>()
            Result.success(response)
        } catch (e: Exception) {
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

                else -> "An expected error occurred : ${e.message}"
            }
            Result.failure(Exception(userMessage))
        }
    }
}