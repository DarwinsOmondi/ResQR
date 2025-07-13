package com.example.resqr.presentation.screen.password

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.resqr.di.AppModule
import com.example.resqr.domain.model.authModel.AuthResponse
import com.example.resqr.domain.model.passwordModel.PasswordResponse
import com.example.resqr.presentation.components.sharedComponents.TopAppBar
import com.example.resqr.presentation.components.victim.SectionHeader
import com.example.resqr.presentation.viewmodel.PasswordViewModel

@Composable
fun SetPasswordScreen(navController: NavController) {
    val passwordViewModel: PasswordViewModel = AppModule.passwordViewModel

    val passwordState by passwordViewModel.passwordState.collectAsState()
    val password by passwordViewModel.password.collectAsState()
    val confirmPassword by passwordViewModel.confirmPassword.collectAsState()
    val isPasswordVisible by passwordViewModel.isPasswordVisible.collectAsState()
    val isConfirmPasswordVisible by passwordViewModel.isConfirmPasswordVisible.collectAsState()
    val isPasswordAvailable by passwordViewModel.isPasswordAvailable.collectAsState()

    val context = LocalContext.current

    val passwordNotNull = password.orEmpty()
    val confirmPasswordNotNull = confirmPassword.orEmpty()
    val isCorrectPassword =
        passwordViewModel.checkPasswordSimilarity(passwordNotNull, confirmPasswordNotNull)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Settings",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            SetPasswordContents(
                onPasswordChange = { passwordViewModel.onPasswordChanged(it) },
                onConfirmPasswordChange = { passwordViewModel.onConfirmPasswordChanged(it) },
                onPasswordVisibilityChange = { passwordViewModel.onPasswordVisibilityChanged() },
                onConfirmPasswordVisibilityChange = { passwordViewModel.onConfirmPasswordVisibilityChanged() },
                password = passwordNotNull,
                confirmPassword = confirmPasswordNotNull,
                isPasswordVisible = isPasswordVisible,
                isConfirmPasswordVisible = isConfirmPasswordVisible,
                isPasswordAvailable = isPasswordAvailable,
                passWordState = passwordState,
                isPasswordCorrect = isCorrectPassword,
                onSetPasswordClick = {
                    if (!isCorrectPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    } else {
                        passwordViewModel.setPassword(userId = 1, password = passwordNotNull)
                    }
                },
                onUpdatePasswordClick = {
                    if (!isCorrectPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    } else {
                        passwordViewModel.updatePassword(userId = 1, password = passwordNotNull)
                    }
                }
            )
        }
    }
}


@Composable
fun SetPasswordContents(
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSetPasswordClick: () -> Unit,
    onUpdatePasswordClick: () -> Unit,
    password: String,
    confirmPassword: String,
    isPasswordVisible: Boolean,
    isConfirmPasswordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    onConfirmPasswordVisibilityChange: () -> Unit,
    isPasswordAvailable: Boolean = false,
    passWordState: PasswordResponse,
    isPasswordCorrect: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionHeader(
            title = "Set Password",
            icon = Icons.Default.Lock,
            tint = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "Secure your account and protect your data with a strong password.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        // Password input
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = onPasswordVisibilityChange) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Confirm password input
        TextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = {
                Text(
                    text = if (confirmPassword.isNotEmpty()) {
                        if (isPasswordCorrect) "Confirm Password ✓" else "Passwords do not match ✗"
                    } else "Confirm Password",
                    color = when {
                        confirmPassword.isEmpty() -> MaterialTheme.colorScheme.onSurface
                        isPasswordCorrect -> Color.Green.copy(alpha = 0.8f)
                        else -> Color.Red.copy(alpha = 0.8f)
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = onConfirmPasswordVisibilityChange) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Action Button
        Button(
            onClick = {
                if (isPasswordAvailable) onUpdatePasswordClick()
                else onSetPasswordClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            val buttonText = when {
                passWordState is PasswordResponse.Loading && isPasswordAvailable -> "Updating..."
                passWordState is PasswordResponse.Loading -> "Saving..."
                isPasswordAvailable -> "Update Password"
                else -> "Set Password"
            }
            Text(text = buttonText, style = MaterialTheme.typography.bodyLarge)
        }

        // Error Feedback
        if (passWordState is PasswordResponse.PasswordError) {
            Text(
                text = passWordState.message,
                color = Color.Red.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SetPasswordScreenPreview() {
    SetPasswordContents(
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onSetPasswordClick = {},
        password = "",
        confirmPassword = "",
        isPasswordVisible = false,
        isConfirmPasswordVisible = false,
        onPasswordVisibilityChange = {},
        onConfirmPasswordVisibilityChange = {},
        onUpdatePasswordClick = {},
        isPasswordAvailable = false,
        passWordState = PasswordResponse.Uninitialized,
        isPasswordCorrect = false,
    )
}