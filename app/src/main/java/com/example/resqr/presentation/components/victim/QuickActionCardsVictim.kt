package com.example.resqr.presentation.components.victim

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuickActionCardVictim(
    icon: ImageVector,
    contentDescription: String,
    title: String,
    onCardClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            hoveredElevation = 8.dp,
            pressedElevation = 6.dp
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(150.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = contentDescription,
                tint = colorScheme.secondary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onSecondaryContainer
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun QuickActionCardsPreview() {
    QuickActionCardVictim(
        icon = Icons.Default.Person,
        contentDescription = "Medical Profile",
        title = "Emergency",
        onCardClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun QuickActionCardsPreview1() {
    QuickActionCardVictim(
        icon = Icons.Default.QrCode,
        contentDescription = "Share QR Code",
        title = "Share QR Code",
        onCardClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun QuickActionCardsPreview2() {
    QuickActionCardVictim(
        icon = Icons.Default.Phone,
        contentDescription = "Call 911",
        title = "Call 911",
        onCardClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun QuickActionCardsPreview3() {
    QuickActionCardVictim(
        icon = Icons.Default.LocationOn,
        contentDescription = "Update Location",
        title = "Update Location",
        onCardClick = {}
    )
}