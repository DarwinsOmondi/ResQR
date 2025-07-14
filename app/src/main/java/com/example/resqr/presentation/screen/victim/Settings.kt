package com.example.resqr.presentation.screen.victim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.resqr.presentation.components.responder.QuickActionCardResponder
import com.example.resqr.presentation.components.sharedComponents.TopAppBar

@Composable
fun SettingsScreen(navController: NavController) {
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
        Column(modifier = Modifier.padding(innerPadding)) {
            SettingsContents(
                onAccountClick = { },
                onPrivacyClick = { navController.navigate("set_password_screen")  },
                onDisplayClick = { /* Handle display click */ },
                onHelpClick = { /* Handle help click */ }
            )
        }
    }
}

@Composable
fun SettingsContents(
    onAccountClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onDisplayClick: () -> Unit = {},
    onHelpClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(128.dp)
                .clip(MaterialTheme.shapes.medium),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Icon",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        QuickActionCardResponder(
            icon = Icons.Default.Key,
            contentDescription = "Account",
            title = "Account",
            content = "Security and account settings",
            iconColor = Color(0xFF1976D2),
            onCardClick = onAccountClick
        )

        QuickActionCardResponder(
            icon = Icons.Default.Lock,
            contentDescription = "Privacy",
            title = "Privacy",
            content = "Lock your account and medical data",
            iconColor = Color(0xFFF44336),
            onCardClick = onPrivacyClick
        )

        QuickActionCardResponder(
            icon = Icons.Default.ColorLens,
            contentDescription = "Display",
            title = "Display",
            content = "Theme and language settings",
            iconColor = Color(0xFF4CAF50),
            onCardClick = onDisplayClick
        )

        QuickActionCardResponder(
            icon = Icons.AutoMirrored.Filled.Help,
            contentDescription = "Help",
            title = "Help",
            content = "Help center, contact us, and privacy policy",
            iconColor = Color(0xFFFFC107),
            onCardClick = onHelpClick
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsContents()
}