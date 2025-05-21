package com.example.resqr.clienthome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.resqr.utils.supabaseClient
import kotlinx.coroutines.delay

@Composable
fun BannerMessage() {
    val supabaseClient = supabaseClient
    val clientRepository = ClientRepository(supabaseClient)
    val clientViewModel: ClientViewmodel = viewModel(factory = ClientHomeFactory(clientRepository))
    val uiState by clientViewModel.uiState.collectAsState()
    var userMessage by remember { mutableStateOf("") }
    var showBanner by remember { mutableStateOf(false) }
    var lastDisplayedMessage by remember { mutableStateOf("") }

    // Update banner when a new message arrives
    LaunchedEffect(uiState) {
        val newMessage = uiState.error ?: uiState.saveSuccess
        if (newMessage != null && newMessage.isNotEmpty() && newMessage != lastDisplayedMessage) {
            userMessage = newMessage
            showBanner = true
            lastDisplayedMessage = newMessage
        }
    }

    if (showBanner) {
        // Auto-dismiss after 4 seconds
        LaunchedEffect(userMessage) {
            delay(4000)
            showBanner = false
            clientViewModel.clearMessage() // Clear message in ViewModel
        }

        BannerCard(
            message = userMessage,
            backgroundColor = Color(0xFFBBDEFB),
            onDismiss = {
                showBanner = false
                clientViewModel.clearMessage() // Clear message in ViewModel
            }
        )
    }
}

@Composable
fun BannerCard(
    message: String,
    backgroundColor: Color = Color(0xFFB3E5FC),
    textColor: Color = Color.Black,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onDismiss() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss"
                )
            }
        }
    }

}