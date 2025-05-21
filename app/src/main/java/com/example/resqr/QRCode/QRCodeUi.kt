package com.example.resqr.QRCode

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.resqr.MainActivity
import com.example.resqr.clientprofile.ClientProfileFactory
import com.example.resqr.clientprofile.ClientProfileRepository
import com.example.resqr.clientprofile.ClientProfileViewModel
import com.example.resqr.utils.QrForegroundService
import com.example.resqr.utils.supabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QRCodeScreen(navController: NavHostController) {
    val supabaseClient = supabaseClient
    val repo = ClientProfileRepository(supabaseClient)
    val profileViewModel: ClientProfileViewModel = viewModel(factory = ClientProfileFactory(repo))
    val userId = supabaseClient.auth.currentUserOrNull()?.id

    LaunchedEffect(Unit) {
        userId?.let { profileViewModel.fetchClientMedicalData(it) }
    }

    val uiState by profileViewModel.uiState.collectAsState()
    val qrCodeRepository = QRCodeRepository()
    val qrCodeViewModel = QRCodeViewModel(qrCodeRepository)
    val context = LocalContext.current
    var isServiceRunning by remember { mutableStateOf(false) }
    var showWarning by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            if (!isServiceRunning) {
                with(NotificationManagerCompat.from(context)) {
                    cancel(1)
                }
            }
        }
    }

    if (showWarning && isServiceRunning) {
        AlertDialog(
            onDismissRequest = { showWarning = false },
            title = { Text("Warning") },
            text = { Text("Displaying your medical QR code on the lock screen may expose sensitive information. Proceed with caution.") },
            confirmButton = {
                Button(onClick = { showWarning = false }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Medical QR Code") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                LoadingShimmerEffect(modifier = Modifier.padding(padding))
            }

            uiState.fetchSuccess != null -> {
                val jsonData = Json.encodeToString(uiState.fetchSuccess)
                // Generate QR code on background thread
                val qrBitmap = remember {
                    kotlinx.coroutines.runBlocking(Dispatchers.Default) {
                        qrCodeViewModel.generateQRCode(jsonData)
                    }
                }
                QRCodeContent(
                    qrBitmap = qrBitmap,
                    userName = uiState.fetchSuccess!!.fullname,
                    bloodType = uiState.fetchSuccess!!.medicalData.blood_type,
                    modifier = Modifier.padding(padding),
                    onSaveClick = { saveQRCodeToDevice(qrBitmap, context) },
                    onShareClick = { shareQRCode(qrBitmap, context) },
                    onToggleService = {
                        if (isServiceRunning) {
                            context.stopService(Intent(context, QrForegroundService::class.java))
                            isServiceRunning = false
                        } else {
                            val file = File(
                                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                "medical_qr_code_${System.currentTimeMillis()}.png"
                            )
                            try {
                                FileOutputStream(file).use { out ->
                                    qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                                }
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    file
                                )
                                val intent =
                                    Intent(context, QrForegroundService::class.java).apply {
                                        putExtra("qrBitmapUri", uri)
                                        putExtra("userName", uiState.fetchSuccess!!.fullname)
                                        putExtra(
                                            "bloodType",
                                            uiState.fetchSuccess!!.medicalData.blood_type
                                        )
                                    }
                                context.startForegroundService(intent)
                                isServiceRunning = true
                                showWarning = true
                            } catch (e: Exception) {
                                android.widget.Toast.makeText(
                                    context,
                                    "Failed to start QR service",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    isServiceRunning = isServiceRunning
                )
            }

            uiState.error != null -> {
                ErrorState(
                    errorMessage = uiState.error!!,
                    onRetry = { userId?.let { profileViewModel.fetchClientMedicalData(it) } },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@androidx.annotation.RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
private fun showQRCodeNotification(
    context: Context,
    qrBitmap: Bitmap,
    userName: String,
    bloodType: String
) {
    val notificationId = 1
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("navigate_to_qr", true)
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, "resqr_qr_channel")
        .setSmallIcon(android.R.drawable.ic_menu_info_details)
        .setContentTitle("Medical QR Code")
        .setContentText("$userName • Blood Type: $bloodType")
        .setLargeIcon(qrBitmap)
        .setStyle(
            NotificationCompat.BigPictureStyle().bigPicture(qrBitmap).bigLargeIcon(null as Bitmap?)
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setAutoCancel(false)
        .setOngoing(true)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .build()

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, notification)
    }
}

@Composable
fun LoadingShimmerEffect(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Generating QR Code...",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.semantics { contentDescription = "Loading QR Code" }
            )
        }
    }
}

@Composable
fun ErrorState(errorMessage: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.semantics { contentDescription = "Error Message" }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }
}

@Composable
fun QRCodeContent(
    qrBitmap: Bitmap,
    userName: String,
    bloodType: String,
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    onToggleService: () -> Unit,
    isServiceRunning: Boolean
) {
    val configuration = LocalConfiguration.current
    val qrSize = (configuration.screenWidthDp * 0.8f).dp.coerceAtMost(300.dp)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your Medical QR Code",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.semantics { contentDescription = "QR Code Title" }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Scan this QR code to share your medical information securely.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .semantics { contentDescription = "QR Code Instructions" }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "QR Code Card" },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "QR code containing your medical information",
                modifier = Modifier
                    .size(qrSize)
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onSaveClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save QR Code")
            }
            Button(
                onClick = onShareClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share QR Code")
            }
        }
        Button(
            onClick = onToggleService,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                if (isServiceRunning) Icons.Default.Close else Icons.Default.Notifications,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isServiceRunning) "Stop QR Notification" else "Show QR on Lock Screen")
        }
    }
}


fun saveQRCodeToDevice(bitmap: Bitmap, context: Context) {
    val file = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "medical_qr_code_${System.currentTimeMillis()}.png"
    )
    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        android.widget.Toast.makeText(
            context,
            "QR Code saved to gallery",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    } catch (e: Exception) {
        android.widget.Toast.makeText(
            context,
            "Failed to save QR Code",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}


fun shareQRCode(bitmap: Bitmap, context: Context) {
    val file = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "medical_qr_code_${System.currentTimeMillis()}.png"
    )
    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
    } catch (e: Exception) {
        android.widget.Toast.makeText(
            context,
            "Failed to share QR Code",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}