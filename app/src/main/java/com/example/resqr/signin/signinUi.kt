package com.example.resqr.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.resqr.utils.supabaseClient
import io.github.jan.supabase.auth.auth

@Composable
fun SignInScreen(navHostController: NavHostController) {
    val repository = SigningRepository(supabaseClient)
    val signinviewmodel: SignInViewmodel = viewModel(factory = SignInFactory(repository))
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordFieldError by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val gradientColors = listOf(
        Color(0xFFFF512F),
        Color(0xFFDD2476)
    )
    val uiState = signinviewmodel.uiState.collectAsState()
    var buttonState by remember { mutableStateOf(false) }
    var passwordErrorColor: Color = if (passwordFieldError.isNotEmpty()) {
        Color.Red
    } else {
        Color.Gray
    }
    LaunchedEffect(Unit) {
        if (supabaseClient.auth.currentUserOrNull() != null) {
            navHostController.navigate("home")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(gradientColors)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                content = {
                    Column {
                        TextButton(
                            onClick = { navHostController.navigate("signup") },
                            content = {
                                Text(
                                    "Sign Up",
                                    style = TextStyle(
                                        fontStyle = FontStyle.Normal,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 24.sp
                                    ),
                                    color = if (navHostController.currentDestination?.route == "signup") {
                                        Color.White
                                    } else {
                                        Color.Black
                                    }
                                )
                            }
                        )
                    }
                    Column {
                        TextButton(
                            onClick = { navHostController.navigate("signin") },
                            content = {
                                Text(
                                    "Sign In",
                                    style = TextStyle(
                                        fontStyle = FontStyle.Normal,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 24.sp
                                    ),
                                    color = if (navHostController.currentDestination?.route == "signin") {
                                        Color.White
                                    } else {
                                        Color.Black
                                    }
                                )
                            }
                        )
                    }
                }
            )
            Spacer(Modifier.height(32.dp))
            Text(
                "ResQR",
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                )
            )
            Spacer(Modifier.height(32.dp))
            Text("Welcome back")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(Color.White),
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White),
                        contentAlignment = Alignment.TopCenter,
                        content = {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                //email
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    label = { Text("Email") },
                                    placeholder = { Text("johndoe@gmail.com") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    ),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color(0xFF6B48FF),
                                        unfocusedIndicatorColor = Color.Black,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                    )
                                )
                                OutlinedTextField(
                                    value = password,
                                    onValueChange = {
                                        password = it
                                        passwordFieldError = if (password.length < 8) {
                                            "Password must be at least  8 characters"
                                        } else {
                                            ""
                                        }
                                    },
                                    label = {
                                        Text(
                                            if (passwordFieldError.isNotEmpty()) passwordFieldError else "Password",
                                            color = passwordErrorColor
                                        )
                                    },
                                    placeholder = { Text("*******") },
                                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password,
                                        imeAction = ImeAction.Next
                                    ),
                                    singleLine = true,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            passwordVisible = !passwordVisible
                                        }) {
                                            Icon(
                                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                                tint = Color.Black
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color(0xFF6B48FF),
                                        unfocusedIndicatorColor = Color.Black,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                    )
                                )
                                Spacer(Modifier.height(16.dp))
                                if (email.isNotEmpty() && password.isNotEmpty()) {
                                    buttonState = true
                                } else {
                                    buttonState
                                }
                                Button(
                                    onClick = {
                                        signinviewmodel.signInUser(
                                            email,
                                            password
                                        )
                                        if (uiState.value.success != null) {
                                            navHostController.navigate("home")

                                        }
                                    },
                                    Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(5.dp),
                                    enabled = buttonState,
                                    colors = ButtonDefaults.buttonColors(
                                        Color(0xFFFF512F)
                                    )
                                ) {
                                    Text(
                                        if (uiState.value.isLoading) "Signing In..." else "Sign In",
                                        color = Color.Black
                                    )
                                }
                                Spacer(Modifier.height(16.dp))
                                uiState.value.error?.let { errorMessage ->
                                    Text(errorMessage, color = Color.Red)
                                }
                            }
                        }
                    )
                }
            )
        }
    )
}