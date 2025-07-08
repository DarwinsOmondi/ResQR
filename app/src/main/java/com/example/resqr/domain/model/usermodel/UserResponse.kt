package com.example.resqr.domain.model.usermodel

import com.example.resqr.domain.model.authModel.User

sealed class UserResponse {
    object Uninitialized : UserResponse()
    object Loading : UserResponse()
    data class GetUser(val user: User?) : UserResponse()
    data class UserError(val message: String) : UserResponse()
}