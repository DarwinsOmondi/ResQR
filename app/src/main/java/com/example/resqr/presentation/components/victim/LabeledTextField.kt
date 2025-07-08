package com.example.resqr.presentation.components.victim

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector
) {
    val colorScheme = MaterialTheme.colorScheme

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = colorScheme.onSurface) },
        leadingIcon = {
            Icon(icon, contentDescription = null, tint = colorScheme.secondary)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorScheme.surface,
            unfocusedContainerColor = colorScheme.surface,
            focusedIndicatorColor = colorScheme.primary,
            unfocusedIndicatorColor = colorScheme.outline,
            focusedTextColor = colorScheme.onSurface,
            unfocusedTextColor = colorScheme.onSurface,
            focusedLabelColor = colorScheme.onSurface,
            unfocusedLabelColor = colorScheme.onSurface,
            cursorColor = colorScheme.primary
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LabeledTextFieldPreview() {
    LabeledTextField(
        label = "Full Name",
        value = "John Doe",
        onValueChange = {},
        icon = Icons.Default.Person
    )
}