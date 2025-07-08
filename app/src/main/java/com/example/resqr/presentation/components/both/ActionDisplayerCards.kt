package com.example.resqr.presentation.components.both

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ActionDisplayerCards(
    icon: ImageVector,
    contentDescription: String,
    title: String,
    content: String,
    iconColor: Color,
    cardColor: Color,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(cardColor),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(cardColor.copy(.5f)),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = iconColor,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = iconColor,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayerCardsPreview() {
    ActionDisplayerCards(
        icon = Icons.Default.HeartBroken,
        contentDescription = "Person Icon",
        title = "Emergency Assistant",
        content = "Tap to speak for",
        iconColor = Color.White,
        cardColor = Color.Red,
        onCardClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DisplayerCardsPreview2() {
    ActionDisplayerCards(
        icon = Icons.Default.Emergency,
        contentDescription = "Emergency Icon",
        title = "Emergency",
        content = "Tap to initiate",
        iconColor = Color.White,
        cardColor = Color.Red,
        onCardClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DisplayerCardsPreview3() {
    ActionDisplayerCards(
        icon = Icons.Default.HealthAndSafety,
        contentDescription = "First Responder",
        title = "First Responder",
        content = "Emergency response",
        iconColor = Color.White,
        cardColor = Color(0xFF1976D2),
        onCardClick = {}
    )
}