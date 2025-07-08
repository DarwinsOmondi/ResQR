package com.example.resqr.presentation.components.responder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun QuickActionCardResponder(
    icon: ImageVector,
    contentDescription: String,
    title: String,
    iconColor: Color,
    cardColor: Color,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(cardColor),
        elevation = CardDefaults.cardElevation(
            4.dp,
            hoveredElevation = 8.dp,
            pressedElevation = 6.dp
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .padding(8.dp)) { }
    }
}