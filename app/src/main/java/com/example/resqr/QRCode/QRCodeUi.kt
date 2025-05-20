package com.example.resqr.QRCode

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.resqr.clientprofile.ClientProfileFactory
import com.example.resqr.clientprofile.ClientProfileRepository
import com.example.resqr.clientprofile.ClientProfileViewModel
import com.example.resqr.utils.supabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeScreen(navController: NavHostController) {
    val context = LocalContext.current
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
                val qrBitmap = remember { qrCodeViewModel.generateQRCode(jsonData) }
                QRCodeContent(
                    qrBitmap = qrBitmap,
                    modifier = Modifier.padding(padding),
                    onSaveClick = { saveQRCodeToDevice(qrBitmap, context) },
                    onShareClick = { shareQRCode(qrBitmap, context) }
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
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit
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
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save QR Code")
            }
            Button(
                onClick = onShareClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share QR Code")
            }
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
        // Notify user of success (e.g., via Toast)
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