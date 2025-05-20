package com.example.resqr.signin

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo

class SigningRepository(private val supabaseClient: SupabaseClient) {

    suspend fun signInUser(email: String, password: String): Result<UserInfo> {

        return try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val user = supabaseClient.auth.currentUserOrNull()
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Sign in failed"))
            }
        } catch (e: Exception) {
            Log.e("SignIn","${e.message}")
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
                    "invalid email",
                    ignoreCase = true
                ) -> "Please enter a valid email address."

                else -> "Something went wrong. Please try again."
            }
            Result.failure(Exception(userMessage))
        }
    }
}