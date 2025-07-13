package com.example.resqr.data.repository

import com.example.resqr.domain.model.authModel.AuthResponse
import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.repository.authRepository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.String

class AuthRepositoryImpl(private val supabaseClient: SupabaseClient) : AuthRepository {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun signUp(
        email: String,
        password: String,
        phoneNumber: Int,
        fullName: String
    ): AuthResponse {
        return try {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = buildJsonObject {
                    put("phoneNumber", JsonPrimitive(phoneNumber))
                    put("fullName", JsonPrimitive(fullName))
                }
            }

            val user = supabaseClient.auth.currentSessionOrNull()?.user
            if (user != null) {
                val currentUser = User(
                    fullName = user.userMetadata?.get("fullName").toString().replace("\"", ""),
                    email = user.email.toString(),
                    phoneNumber = user.userMetadata?.get("phoneNumber").toString().replace("\"", "")
                )
                AuthResponse.GetAuthUser(currentUser)
            } else {
                AuthResponse.AuthError("User signed up but not found in session.")
            }
        } catch (e: Exception) {
            AuthResponse.AuthError(e.message ?: "Unknown Error")
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): AuthResponse {
        return try {
            // Sign in
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val user = supabaseClient.auth.currentSessionOrNull()?.user
            if (user != null) {
                val currentUser = User(
                    fullName = user.userMetadata?.get("fullName").toString().replace("\"", ""),
                    email = user.email.toString(),
                    phoneNumber = user.userMetadata?.get("phoneNumber").toString().replace("\"", "")
                )
                AuthResponse.GetAuthUser(currentUser)
            } else {
                AuthResponse.AuthError("User signed in but not found in session.")
            }
        } catch (e: Exception) {
            val message = e.message ?: ""
            val userMessage = when {
                message.contains(
                    "invalid login credentials",
                    ignoreCase = true
                ) -> "Incorrect Email or Password"

                message.contains(
                    "network",
                    ignoreCase = true
                ) -> "Please check your internet connection"

                message.contains(
                    "invalid email",
                    ignoreCase = true
                ) -> "Please enter a valid email address."

                message.contains(
                    "missing email or phone",
                    ignoreCase = true
                ) -> "Email or phone is missing"

                else -> "Something went wrong. Please try again."
            }
            AuthResponse.AuthError(userMessage)
        }
    }

    override suspend fun getCurrentUser(): AuthResponse {
        return try {
            val user = supabaseClient.auth.currentSessionOrNull()?.user
            if (user != null) {
                val currentUser = User(
                    fullName = user.userMetadata?.get("fullName").toString().replace("\"", ""),
                    email = user.email.toString(),
                    phoneNumber = user.userMetadata?.get("phoneNumber").toString().replace("\"", "")
                )
                AuthResponse.GetAuthUser(currentUser)
            } else {
                AuthResponse.AuthError("")
            }
        } catch (e: Exception) {
            AuthResponse.AuthError(e.message ?: "Unknown Error")
        }
    }
}
