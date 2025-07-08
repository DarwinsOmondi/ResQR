package com.example.resqr.presentation.screen.victim

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqr.presentation.components.both.ActionDisplayerCards
import com.example.resqr.presentation.components.victim.QuickActionCardVictim
import com.example.resqr.presentation.components.victim.VoiceCommandsCard

@Composable
fun VictimHomeScreen() {
    VictimHomeScreenContent()
}

@Composable
fun VictimHomeScreenContent() {
    val scrollState = rememberScrollState()
    val actions = listOf(
        Triple(Icons.Default.Person, "Medical Profile", "Medical Profile"),
        Triple(Icons.Default.QrCode, "Share QR Code", "Share QR Code"),
        Triple(Icons.Default.Phone, "Call 911", "Call 911"),
        Triple(Icons.Default.LocationOn, "Update Location", "Update Location")
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(state = scrollState, enabled = true, orientation = Orientation.Vertical)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionDisplayerCards(
            icon = Icons.Default.Favorite,
            contentDescription = "Person Icon",
            title = "Emergency Assistant",
            content = "Tap to speak for",
            iconColor = Color.White,
            cardColor = Color.Red,
            onCardClick = {}
        )
        ActionDisplayerCards(
            icon = Icons.Default.WarningAmber,
            contentDescription = "Emergency Icon",
            title = "Emergency",
            content = "Tap to initiate",
            iconColor = Color.White,
            cardColor = Color.Red,
            onCardClick = {}
        )
        Text(
            "Quick Actions",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(actions.size) { index ->
                QuickActionCardVictim(
                    icon = actions[index].first,
                    contentDescription = actions[index].second,
                    title = actions[index].third,
                    iconColor = Color(0xFF1976D2),
                    cardColor = Color.White,
                    onCardClick = {

                    }
                )
            }
        }
        VoiceCommandsCard(
            onVoiceCommandClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VictimHomeScreenPreview() {
    VictimHomeScreenContent()
}