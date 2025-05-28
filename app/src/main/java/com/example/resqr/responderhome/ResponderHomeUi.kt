package com.example.resqr.responderhome

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.resqr.clienthome.BannerMessage
import com.example.resqr.clientprofile.LoadingShimmerEffect
import com.example.resqr.model.Alert
import com.example.resqr.ui.theme.ResponderTheme
import com.example.resqr.utils.supabaseClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResponderHomeUi(navHostController: NavHostController, onScanQrCode: () -> Unit) {
    val context = LocalContext.current
    val supabaseClient = supabaseClient
    val responderRepository = ResponderRepository(supabaseClient)
    val responderViewModel: ResponderViewModel = viewModel(
        factory = ResponderFactory(responderRepository)
    )
    val uiState by responderViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        responderViewModel.fetchAlertData()
    }
    ResponderTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Hello!") },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        scrolledContainerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.primary,
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Header
                        BannerMessage()
                        Box(
                            Modifier
                                .fillMaxSize()
                                .weight(.5f)
                                .background(MaterialTheme.colorScheme.primary)
                                .align(Alignment.CenterHorizontally),
                            content = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = "RESPONDER DASHBOARD",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 28.sp
                                        ),
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    )
                                }
                            },
                            contentAlignment = Alignment.Center
                        )

                        // Alerts Section
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Active Alerts Header
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Active Alerts",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Text(
                                        text = uiState.fetchSuccess.size.toString(),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.error,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.errorContainer,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                // Alerts List
                                when {
                                    uiState.isLoading -> {
                                        CircularProgressIndicator()
                                    }

                                    uiState.fetchSuccess.isNotEmpty() -> {
                                        LazyColumn(
                                            verticalArrangement = Arrangement.spacedBy(16.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp)
                                        ) {
                                            items(uiState.fetchSuccess) { alert ->
                                                AlertCard(
                                                    alert = alert,
                                                    viewModel = responderViewModel,
                                                    context = context,
                                                    onRespond = {
                                                        coroutineScope.launch {
                                                            responderViewModel.respondToAlert(alert.id)
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    else -> {
                                        Text(
                                            text = "No active alerts",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        onScanQrCode()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 32.dp, vertical = 16.dp)
                                        .height(56.dp)
                                        .shadow(8.dp, RoundedCornerShape(12.dp)),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.QrCodeScanner,
                                            contentDescription = "Scan qr code",
                                            tint = Color.White
                                        )
                                        Text(
                                            text = "SCAN QR CODE",
                                            style = TextStyle(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertCard(
    alert: Alert,
    viewModel: ResponderViewModel,
    context: Context,
    onRespond: () -> Unit
) {
    var address by remember { mutableStateOf("Loading address...") }

    LaunchedEffect(alert.latitude, alert.longitude) {
        address = viewModel.getAddressFromLatLng(alert.latitude, alert.longitude, context)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(300))
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = address,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Status: ${if (alert.resolved == "true") "Resolved" else "Unresolved"}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (alert.resolved == "true") Color.Green else Color.Red
                )
            )
            Text(
                text = "Sent: ${viewModel.calculateTimeDifference(alert.alertSentAt)}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            if (alert.resolved == "false") {
                Button(
                    onClick = onRespond,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Respond",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}