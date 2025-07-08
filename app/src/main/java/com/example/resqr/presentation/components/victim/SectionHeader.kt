package com.example.resqr.presentation.components.victim

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
fun SectionHeader(title: String, icon: ImageVector? = null, tint: Color) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = title,
                tint = tint
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(color = colorScheme.onBackground),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    SectionHeader(
        "Basic Information",
        icon = Icons.Default.Person,
        tint = MaterialTheme.colorScheme.primary
    )
}