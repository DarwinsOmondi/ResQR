package com.example.resqr.presentation.components.victim

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun EmergencyAlertDialogWrapper(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onCall911: () -> Unit,
    onCancelAlert: () -> Unit,
    remainingTime: Int,
    location: String,
    accuracy: String = "±100m"
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            EmergencyAlertDialog(
                onDismiss = onDismissRequest,
                onCall911 = onCall911,
                onCancelAlert = onCancelAlert,
                remainingTime = remainingTime,
                location = location,
                accuracy = accuracy
            )
        }
    }
}
