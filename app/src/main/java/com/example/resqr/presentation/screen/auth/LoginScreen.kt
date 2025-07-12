package com.example.resqr.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.resqr.di.AppModule
import com.example.resqr.domain.model.authModel.AuthResponse
import com.example.resqr.presentation.viewmodel.AuthViewModel

@Composable
fun LogIn(navController: NavController) {
    val authViewModel: AuthViewModel = AppModule.authViewModel
    val authUiState = authViewModel.authState.collectAsState()
    LogInContent(authUiState.value, authViewModel, navController)
}

@Composable
fun LogInContent(
    authUiState: AuthResponse,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val email = authViewModel.email.collectAsState()
    val password = authViewModel.password.collectAsState()
    val passwordVisibility = authViewModel.passwordVisibility.collectAsState()
    val selectedIndex = authViewModel.selectedIndex.collectAsState()
    val options = listOf(AuthScreenOptions.Login, AuthScreenOptions.SignUp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Icon(
            imageVector = Icons.Default.HealthAndSafety,
            contentDescription = "App Logo",
            modifier = Modifier.size(50.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("ResQR", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))
        Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Welcome Back , Please enter Your details",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sign In / Signup Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1), RoundedCornerShape(50)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEachIndexed { index, label ->
                Button(
                    onClick = {
                        authViewModel.onSelectedIndexChanged(index)
                        val destination = if (index == 0) "login" else "signup"
                        navController.navigate(destination) {
                            popUpTo(destination) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (index == selectedIndex.value) Color.White else Color(
                            0xFFF1F1F1
                        ),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = label.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Email Input
        OutlinedTextField(
            value = email.value,
            onValueChange = { authViewModel.onEmailChange(it) },
            label = { Text("Email Address") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color(0xFF6B48FF),
                unfocusedIndicatorColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        OutlinedTextField(
            value = password.value,
            onValueChange = { authViewModel.onPasswordChange(it) },
            label = { Text("Password") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
                IconButton(onClick = { authViewModel.togglePasswordVisibility() }) {
                    Icon(
                        imageVector = if (passwordVisibility.value) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password"
                    )
                }
            },
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color(0xFF6B48FF),
                unfocusedIndicatorColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Continue Button
        Button(
            onClick = {
                authViewModel.signIn(email.value, password.value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0066FF),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (authUiState is AuthResponse.Loading) {
                Text("Loading...", style = MaterialTheme.typography.bodyLarge)
            } else {
                Text("Continue", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Social Login Text
        Text("Or Continue With", color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Social Icons Row
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(
                "G" to Color.White,
                "A" to Color.Black,
                "F" to Color(0xFF3b5998)
            ).forEach { (label, bgColor) ->
                Button(
                    onClick = { /* Handle social login */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = bgColor,
                        contentColor = if (bgColor == Color.White) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.size(56.dp)
                ) {
                    Text(label)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (authUiState) {
            is AuthResponse.AuthError -> {
                Text(
                    text = authUiState.message,
                    color = Color.Red.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            is AuthResponse.GetAuthUser -> {
                if (authUiState.user != null) {
                    navController.navigate("victim_home_screen") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            else -> {}
        }
    }
}