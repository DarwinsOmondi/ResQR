package com.example.resqr.presentation.screen.shared

import android.R.attr.contentDescription
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.resqr.di.AppModule
import com.example.resqr.domain.model.authModel.AuthResponse


@Composable
fun SplashScreen(navController: NavController) {
    val authViewModel = AppModule.authViewModel
    val medicalViewModel = AppModule.medicalViewModel
    val authUiState = authViewModel.authState.collectAsState()
    LaunchedEffect(Unit) {

        authViewModel.getCurrentUser()
    }

    // Navigate based on user state
    LaunchedEffect(authUiState.value) {
        when (val state = authUiState.value) {
            is AuthResponse.GetAuthUser -> {
                if (state.user != null) {
                    navController.navigate("victim_home_screen") {
                        popUpTo("splash") { inclusive = true }
                    }
                } else {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }

            is AuthResponse.AuthError -> {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }

            else -> {}
        }
    }
    SplashScreenContent(authUiState.value)
}

@Composable
fun SplashScreenContent(state: AuthResponse) {
    val backgroundColor = Color(0xFF0118D8).copy(alpha = 0.9f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .animateContentSize()
        ) {
            // Fade-in animation for the app name
            val alpha by rememberInfiniteTransition().animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Text(
                text = "ResQR",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                color = Color.White.copy(alpha = alpha)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Instant emergency alerts\nSecure medical data sharing",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            if (state is AuthResponse.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 4.dp,
                    modifier = Modifier
                        .size(40.dp)
//                        .semantics { contentDescription = 156 }
                )
            }
        }
    }
}