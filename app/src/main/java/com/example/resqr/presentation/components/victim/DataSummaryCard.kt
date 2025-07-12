package com.example.resqr.presentation.components.victim

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sick
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.resqr.domain.model.medicalRecordModel.Allergy
import com.example.resqr.domain.model.medicalRecordModel.Medication

@Composable
fun DataSummaryCard(
    bloodType: String,
    allergies: List<Allergy>,
    medications: List<Medication>,
    emergencyContact: String
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Medical Summary",
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                icon = Icons.Default.Bloodtype,
                label = "Blood Type",
                value = bloodType
            )
            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                icon = Icons.Default.Sick,
                label = "Allergies",
                value = allergies.joinToString(", ") { it.substance }.ifEmpty { "None" }
            )
            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                icon = Icons.Default.Vaccines,
                label = "Medications",
                value = medications.joinToString(", ") { it.name }.ifEmpty { "None" }
            )
            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                icon = Icons.Default.Phone,
                label = "Emergency Contact",
                value = emergencyContact
            )
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = colorScheme.error,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurface,
            modifier = Modifier.width(130.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurfaceVariant,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDataSummaryCard() {
    DataSummaryCard(
        bloodType = "O+",
        allergies = listOf(Allergy("Peanut"), Allergy("Dust")),
        medications = listOf(Medication("Ibuprofen"), Medication("Metformin")),
        emergencyContact = "+254 700 123456"
    )
}
