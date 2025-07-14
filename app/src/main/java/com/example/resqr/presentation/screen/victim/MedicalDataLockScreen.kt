package com.example.resqr.presentation.screen.victim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.resqr.di.AppModule
import com.example.resqr.domain.model.passwordModel.PasswordResponse
import com.example.resqr.presentation.components.sharedComponents.BottomNavBar
import com.example.resqr.presentation.components.sharedComponents.TopAppBar
import com.example.resqr.presentation.viewmodel.PasswordViewModel

@Composable
fun QrLockScreen(navController: NavController) {
    val passwordViewModel: PasswordViewModel = AppModule.passwordViewModel
    val unLockPassword by passwordViewModel.unLockPassword.collectAsState()
    val passWordVisibility by passwordViewModel.isPasswordVisible.collectAsState()
    val isUnlocked by passwordViewModel.isUnlocked.collectAsState()
    val passWordState by passwordViewModel.passwordState.collectAsState()
    val buttonState = unLockPassword != null

    LaunchedEffect(isUnlocked) {
        if (isUnlocked) {
            navController.navigate("show_qr")
            passwordViewModel.resetUnlockState()
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 8.dp)
        ) {
            MedicalDataLockScreen(
                password = unLockPassword ?: "",
                onPasswordChange = { passwordViewModel.onUnlockPasswordChanged(it) },
                onPasswordSubmit = {
                    passwordViewModel.isPasswordCorrect(1, unLockPassword ?: "")
                },
                isPasswordVisible = passWordVisibility,
                onPasswordVisibilityChange = { passwordViewModel.togglePasswordVisibility() },
                passwordState = passWordState,
                buttonState = buttonState
            )
        }
    }
}

@Composable
fun MedicalDataLockScreen(
    passwordState: PasswordResponse,
    password: String,
    onPasswordChange: (String) -> Unit,
    onPasswordSubmit: () -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    buttonState: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Unlock your Medical Data",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            TextField(
                maxLines = 1,
                value = password,
                onValueChange = onPasswordChange,
                label = {
                    Text("Password")
                },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("${password.length}/10")
                        IconButton(onClick = onPasswordVisibilityChange) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Button(
                enabled = buttonState,
                onClick = { onPasswordSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (passwordState is PasswordResponse.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Submit")
                }
            }

            when (passwordState) {
                is PasswordResponse.PasswordError -> {
                    Text(
                        passwordState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordScreenPreview() {
    MedicalDataLockScreen(
        password = "",
        onPasswordChange = {},
        onPasswordSubmit = {},
        isPasswordVisible = true,
        onPasswordVisibilityChange = {},
        passwordState = PasswordResponse.Uninitialized,
        buttonState = true
    )
}