package com.example.resqr.domain.usecase.user

data class UserUseCase(
    val insertUser: InsertUserUseCase,
    val getUser: GetUserUseCase,
    val updateUser: UpdateUserUseCase,
    val deleteUser: DeleteUserUseCase
)