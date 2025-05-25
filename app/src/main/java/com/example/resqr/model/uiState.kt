package com.example.resqr.model

import io.github.jan.supabase.auth.user.UserInfo

data class UiState(
    val isLoading: Boolean = false,
    val saveSuccess: String? = null,
    val fetchSuccess: User? = null,
    val alertSuccess: Alert? = null,
    val error: String? = null
)

data class AuthUiState(
    val isLoading: Boolean = false,
    val success: UserInfo? = null,
    val error: String? = null
)