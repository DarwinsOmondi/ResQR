package com.example.resqr.presentation.screen.responder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.resqr.presentation.components.both.ActionDisplayerCards
import com.example.resqr.presentation.components.both.BottomNavBar
import com.example.resqr.presentation.components.responder.MedicalInformationCard
import com.example.resqr.presentation.components.responder.PriorityAssessmentCard
import com.example.resqr.presentation.components.responder.QuickActionCardResponder

@Composable
fun ResponderHomeScreen(navController: NavController) {
    ResponderHomeScreenContent(navController)
}

@Composable
fun ResponderHomeScreenContent(navController: NavController) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionDisplayerCards(
                icon = Icons.Default.HealthAndSafety,
                contentDescription = "First Responder",
                title = "First Responder",
                content = "Emergency response",
                iconColor = Color.White,
                cardColor = Color(0xFF1976D2),
                onCardClick = {}
            )

            Text(
                "Quick Actions",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )

            QuickActionCardResponder(
                icon = Icons.Default.QrCode,
                contentDescription = "Scan QR",
                title = "Scan QR Code",
                content = "Get victim medical data",
                iconColor = Color(0xFF1976D2),
                cardColor = Color.White,
                onCardClick = { /* Navigate */ }
            )

            QuickActionCardResponder(
                icon = Icons.Default.Phone,
                contentDescription = "Call",
                title = "Call Dispatch",
                content = "Contact emergency services",
                iconColor = Color(0xFFF44336),
                cardColor = Color.White,
                onCardClick = { /* Navigate */ }
            )

            QuickActionCardResponder(
                icon = Icons.Default.LocationOn,
                contentDescription = "Location",
                title = "Share Location",
                content = "Send current location",
                iconColor = Color(0xFF4CAF50),
                cardColor = Color.White,
                onCardClick = { /* Navigate */ }
            )

            QuickActionCardResponder(
                icon = Icons.Default.Update,
                contentDescription = "Update",
                title = "Update Status",
                content = "Report situation status",
                iconColor = Color(0xFFFFC107),
                cardColor = Color.White,
                onCardClick = { /* Navigate */ }
            )
            Text(
                "Response Guidelines",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )

            PriorityAssessmentCard()
            MedicalInformationCard()
        }
    }
}