package com.example.resqr.domain.usecase.password

data class PasswordUseCase(
    val getPassword: GetPasswordUseCase,
    val savePassword: SavePasswordUseCase,
    val deletePassword: DeletePasswordUseCase,
    val updatePassword: UpdatePasswordUseCase,
    val isPasswordCorrect: IsPasswordCorrectUseCase

)