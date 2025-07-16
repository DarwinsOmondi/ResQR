package com.example.resqr.domain.usecase.password

data class Password(
    val getPassword: GetPassword,
    val savePassword: SavePassword,
    val deletePassword: DeletePassword,
    val updatePassword: UpdatePassword,
    val isPasswordCorrect: IsPasswordCorrect

)