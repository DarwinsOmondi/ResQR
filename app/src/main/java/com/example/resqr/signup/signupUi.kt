package com.example.resqr.signup

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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(navHostController: NavHostController) {
    val repository = SignupRepository(supabaseClient)
    val signupviemodel: SignUpViewmodel = viewModel(
        factory = SignupFactory(repository)
    )
    val gradientColors = listOf(
        Color(0xFFFF512F),
        Color(0xFFDD2476)
    )
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var institutionName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var institutionPhoneNumber by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var phoneFieldError by remember { mutableStateOf("") }
    var passwordFieldError by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var dropdownState by remember { mutableStateOf(false) }
    var buttonState by remember { mutableStateOf(true) }
    var termsAndCondtion by remember { mutableStateOf(false) }
    var color: Color = if (navHostController.currentDestination?.route == "signup") {
        Color.Red
    } else {
        Color.Black
    }
    val uiState by signupviemodel.uiState.collectAsState()
    var phoneErrorColor: Color = if (phoneFieldError.isNotEmpty()) {
        Color.Red
    } else {
        Color.Gray
    }
    var passwordErrorColor: Color = if (passwordFieldError.isNotEmpty()) {
        Color.Red
    } else {
        Color.Gray
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
                            onClick = {
                                navHostController.navigate("signup")
                            },
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
            Text("Create your account today")

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
                                //user category(role)
                                OutlinedTextField(
                                    value = role,
                                    onValueChange = { role = it },
                                    label = { Text("Role") },
                                    placeholder = { Text("victim") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    singleLine = true,
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color(0xFF6B48FF),
                                        unfocusedIndicatorColor = Color.Black,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                    ),
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                dropdownState = true
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.ArrowDropDown,
                                                contentDescription = "Dropdown role",
                                                tint = Color.Black
                                            )
                                        }
                                    }
                                )

                                if (role == "Responder") {
                                    OutlinedTextField(
                                        value = institutionName,
                                        onValueChange = { institutionName = it },
                                        label = { Text("Institution") },
                                        placeholder = { Text("Kenya Red Cross") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
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
                                        value = region,
                                        onValueChange = { region = it },
                                        label = { Text("Region") },
                                        placeholder = { Text("Nairobi") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
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
                                        value = institutionPhoneNumber,
                                        onValueChange = {
                                            institutionPhoneNumber = it
                                            phoneFieldError =
                                                if (institutionPhoneNumber.length < 10) {
                                                    "Phone number must be 10 numbers"
                                                } else {
                                                    ""
                                                }
                                        },
                                        label = {
                                            Text(
                                                if (phoneFieldError.isNotEmpty()) phoneFieldError else "Contact Number",
                                                color = phoneErrorColor
                                            )
                                        },
                                        placeholder = { Text("0712345678") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
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
                                        placeholder = { Text("********") },
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
                                } else {
                                    //fullname
                                    OutlinedTextField(
                                        value = fullName,
                                        onValueChange = { fullName = it },
                                        label = { Text("Fullname") },
                                        placeholder = { Text("John Doe") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
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
                                        value = phoneNumber,
                                        onValueChange = {
                                            phoneNumber = it
                                            phoneFieldError =
                                                if (phoneNumber.length < 10) {
                                                    "Phone number must be 10 numbers"
                                                } else {
                                                    ""
                                                }
                                        },
                                        label = {
                                            Text(
                                                if (phoneFieldError.isNotEmpty()) phoneFieldError else "PhoneNumber",
                                                color = phoneErrorColor
                                            )
                                        },
                                        placeholder = { Text("0712345678") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
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

                                    DropdownMenu(
                                        expanded = dropdownState,
                                        onDismissRequest = { dropdownState = false }
                                    ) {
                                        listOfRoles.forEach {
                                            DropdownMenuItem(
                                                text = { Text(it) },
                                                onClick = {
                                                    role = it
                                                    dropdownState = false
                                                }
                                            )
                                        }
                                    }

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
                                        placeholder = { Text("********") },
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
                                }
                                Spacer(Modifier.height(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Checkbox(
                                        checked = termsAndCondtion,
                                        onCheckedChange = { termsAndCondtion = it }
                                    )
                                    Text("Terms and  Conditions")
                                }
//                                if (email.isNotEmpty() && fullName.isNotEmpty() && phoneNumber.isNotEmpty() && role.isNotEmpty() && password.isNotEmpty() && termsAndCondtion == true) {
//                                    buttonState = true
//                                } else {
//                                    buttonState
//                                }
                                Button(
                                    onClick = {
                                        if (role == "Responder") {
                                            signupviemodel.signUpInstitution(
                                                email,
                                                institutionName,
                                                institutionPhoneNumber,
                                                password,
                                                role,
                                                region,
                                            )
                                            if (uiState.success != null) {
                                                navHostController.navigate("permission_request") {
                                                    popUpTo("signup") { inclusive = true }
                                                }
                                            }
                                        } else {
                                            signupviemodel.signUpUser(
                                                email,
                                                fullName,
                                                phoneNumber,
                                                password,
                                                role
                                            )
                                            if (uiState.success != null) {
                                                navHostController.navigate("permission_request") {
                                                    popUpTo("signup") { inclusive = true }
                                                }
                                            }
                                        }

                                    },
                                    Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(5.dp),
                                    enabled = buttonState,
                                    content = {
                                        Text(if (uiState.isLoading) "Signing Up..." else "Sign Up")
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        Color(0xFFFF512F)
                                    )
                                )
                                uiState.error?.let { errorMessage ->
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

//dropdown titles
val listOfRoles = listOf<String>(
    "Victim",
    "Responder"
)