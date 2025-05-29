package com.example.resqr.signup

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class SignupRepository(private val supabaseClient: SupabaseClient) {

    //sign up user
    suspend fun signUpUser(
        email: String,
        fullName: String,
        phoneNumber: String,
        password: String,
        role: String
    ): Result<UserInfo> {
        return try {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("fullname", JsonPrimitive(fullName))
                    put("phoneNumber", JsonPrimitive(phoneNumber.toLong()))
                    put("role", JsonPrimitive(role))
                }
            }
            val userInfo = supabaseClient.auth.currentUserOrNull()
            if (userInfo != null) {
                Result.success(userInfo)
            } else {
                Result.failure(Exception("Sign Up failed"))
            }
        } catch (e: Exception) {
            Log.e("SignUP", "${e.message}")
            val message = e.message ?: ""
            val userMessage = when {
                message.contains(
                    "Invalid login credentials",
                    ignoreCase = true
                ) -> "Incorrect Email or Password"

                message.contains(
                    "network",
                    ignoreCase = true
                ) -> "Please check your internet connection"

                message.contains(
                    "User already registered",
                    ignoreCase = true
                ) -> "This Email is already in use."

                message.contains(
                    "invalid email",
                    ignoreCase = true
                ) -> "Please enter a valid email address."

                else -> "Something went wrong. Please try again."
            }
            Result.failure(Exception(userMessage))
        }
    }


    suspend fun signUpInstitution(
        email: String,
        institutionName: String,
        institutionPhoneNumber: String,
        password: String,
        role: String,
        region: String
    ): Result<UserInfo> {
        return try {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("institutionName", JsonPrimitive(institutionName))
                    put("institutionPhoneNumber", JsonPrimitive(institutionPhoneNumber.toLong()))
                    put("role", JsonPrimitive(role))
                    put("region", JsonPrimitive(region))
                }
            }
            val userInfo = supabaseClient.auth.currentUserOrNull()
            if (userInfo != null) {
                Result.success(userInfo)
            } else {
                Result.failure(Exception("Sign Up failed"))
            }
        } catch (e: Exception) {
            Log.e("SignUP", "${e.message}")
            val message = e.message ?: ""
            val userMessage = when {
                message.contains(
                    "Invalid login credentials",
                    ignoreCase = true
                ) -> "Incorrect Email or Password"

                message.contains(
                    "network",
                    ignoreCase = true
                ) -> "Please check your internet connection"

                message.contains(
                    "User already registered",
                    ignoreCase = true
                ) -> "This Email is already in use."

                message.contains(
                    "invalid email",
                    ignoreCase = true
                ) -> "Please enter a valid email address."

                else -> "Something went wrong. Please try again."
            }
            Result.failure(Exception(userMessage))
        }
    }
}