package com.example.resqr.presentation.screen.password

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.resqr.domain.model.passwordModel.PasswordResponse
import com.example.resqr.presentation.components.sharedComponents.TopAppBar
import com.example.resqr.presentation.components.victim.SectionHeader
import com.example.resqr.presentation.viewmodel.PasswordViewModel
import com.example.resqr.presentation.viewmodel.UserViewModel

@Composable
fun SetPasswordScreen(navController: NavController) {
    val passwordViewModel: PasswordViewModel = AppModule.passwordViewModel

    val passwordState by passwordViewModel.passwordState.collectAsState()
    val password by passwordViewModel.password.collectAsState()
    val confirmPassword by passwordViewModel.confirmPassword.collectAsState()
    val isPasswordVisible by passwordViewModel.isPasswordVisible.collectAsState()
    val isConfirmPasswordVisible by passwordViewModel.isConfirmPasswordVisible.collectAsState()
    val isPasswordAvailable by passwordViewModel.isPasswordAvailable.collectAsState()
    val buttonState = password != null && confirmPassword != null
    val userViewModel: UserViewModel = AppModule.userViewModel
    val backGroundPasswordState by passwordViewModel.backGroundPassword.collectAsState()
    val backGroundPasswordIsNotNull = backGroundPasswordState.orEmpty()
    LaunchedEffect(Unit) {
        userViewModel.getUser()
        passwordViewModel.resetUnlockState()
    }

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
                onBackClick = { navController.popBackStack() },
                actions = {
                    AssistChip(
                        onClick = {
                            passwordViewModel.updatePassword(
                                userId = 1,
                                password = backGroundPasswordIsNotNull,
                                enabled = !isPasswordAvailable
                            )
                        },
                        label = {
                            if (isPasswordAvailable) {
                                Text("Unlock Medical data")
                            } else {
                                Text("Lock Medical data")
                            }
                        },
                        leadingIcon = {
                            if (isPasswordAvailable) {
                                Icon(
                                    Icons.Filled.Lock,
                                    contentDescription = "Localized description",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            } else {
                                Icon(
                                    Icons.Filled.LockOpen,
                                    contentDescription = "Localized description",
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            }
                        }
                    )
                }
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
                        passwordViewModel.setPassword(userId = 1, password = passwordNotNull, true)
                    }
                },
                onUpdatePasswordClick = {
                    if (!isCorrectPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    } else {
                        passwordViewModel.updatePassword(
                            userId = 1,
                            password = passwordNotNull,
                            true
                        )
                    }
                },
                buttonState = buttonState,
                onClearFields = {
                    passwordViewModel.resetTextFields()
                },
                onDeletePasswordClick = {
                    passwordViewModel.deletePassword(userId = 1)
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
    onDeletePasswordClick: () -> Unit,
    isPasswordAvailable: Boolean = false,
    passWordState: PasswordResponse,
    isPasswordCorrect: Boolean = false,
    onClearFields: () -> Unit,
    buttonState: Boolean
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
            text = "Secure your medical data and protect it with a strong password.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("${confirmPassword.length}/10")
                    IconButton(onClick = onConfirmPasswordVisibilityChange) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Button(
            enabled = buttonState,
            onClick = {
                if (isPasswordAvailable) onUpdatePasswordClick()
                else onSetPasswordClick()

                onClearFields()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            val buttonText = if (isPasswordAvailable) "Update Password" else "Set Password"
            Text(text = buttonText, style = MaterialTheme.typography.bodyLarge)
        }
        if (isPasswordAvailable) {
            OutlinedButton(
                onClick = onDeletePasswordClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color.Red),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete Password")
            }

        }

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
        buttonState = false,
        onClearFields = {},
        onDeletePasswordClick = {}
    )
}