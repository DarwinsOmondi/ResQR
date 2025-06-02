package com.example.resqr.responderhome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MedicalServices
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
import com.example.resqr.model.User
import com.example.resqr.model.UserMedicalData
import com.example.resqr.ui.theme.ResponderTheme
import com.example.resqr.utils.supabaseClient
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResponderHomeUi(
    navHostController: NavHostController, qrScanLauncher: ActivityResultLauncher<Intent>,
    scannedResult: State<String?>
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val supabaseClient = supabaseClient
    val responderRepository = ResponderRepository(supabaseClient)
    val responderViewModel: ResponderViewModel = viewModel(
        factory = ResponderFactory(responderRepository)
    )
    val uiState by responderViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showLogOutDialog by remember { mutableStateOf(false) }

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
                    ),
                    actions = {
                        IconButton(onClick = {
                            showLogOutDialog = true
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (showLogOutDialog) {
                            AlertDialog(
                                title = { Text("Are you sure you want to logout?") },
                                onDismissRequest = { showLogOutDialog = false },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            responderViewModel.signOutResponder()
                                            navHostController.navigate("signin") { popUpTo(0) }
                                        },
                                        content = {
                                            Text("Log Out")
                                        }
                                    )
                                },
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            showLogOutDialog = false
                                        },
                                        content = {
                                            Text("Cancel")
                                        }
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                icon = {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Logout,
                                        contentDescription = "Logout"
                                    )
                                }
                            )
                        }
                    }
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
                                        // onScanQrCode()
                                        //  navHostController.navigate("qr_scanner_screen")
                                        val integrator = IntentIntegrator(activity)
                                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                                        integrator.setPrompt("Scan a QR Code")
                                        integrator.setCameraId(0)
                                        integrator.setBeepEnabled(true)
                                        integrator.setOrientationLocked(true)
                                        integrator.setBarcodeImageEnabled(true)
                                        integrator.addExtra(
                                            "android.intent.extra.screenOrientation",
                                            android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                        )

                                        val scanIntent = integrator.createScanIntent()
                                        qrScanLauncher.launch(scanIntent)
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

                                val user: User? = try {
                                    scannedResult.value?.let {
                                        Json.decodeFromString<User>(
                                            it
                                        )
                                    }
                                } catch (e: Exception) {
                                    null // or log the error
                                }
                                if (user != null) {
                                    InfoCard(user)
                                } else {
                                    "User Medical Report error"
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun InfoCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .semantics { contentDescription = "Medical Information Card" },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Medical Profile",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Basic Information",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Blood Type: ${user.medicalData.blood_type}")
            Text("Gender: ${user.medicalData.gender}")
            Text("Conditions: ${user.medicalData.conditions}")

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Allergies",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (user.medicalData.allergies.isNotEmpty()) {
                user.medicalData.allergies.forEach {
                    Text(
                        text = "• ${it.substance}: ${it.reaction}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                Text("None", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Medications",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (user.medicalData.medications.isNotEmpty()) {
                user.medicalData.medications.forEach {
                    Text(
                        text = "• ${it.name}: ${it.dosage}, ${it.frequency}, ${it.duration}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                Text("None", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Emergency Contacts",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (user.medicalData.emergency_contact.isNotEmpty()) {
                user.medicalData.emergency_contact.forEach {
                    Text(
                        text = "• ${it.name}: ${it.phone_number}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                Text("None", style = MaterialTheme.typography.bodySmall)
            }
        }
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
                text = "0${alert.phoneNumber}",
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