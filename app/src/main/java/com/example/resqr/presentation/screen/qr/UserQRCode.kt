package com.example.resqr.presentation.screen.qr

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.resqr.di.AppModule
import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.model.medicalRecordModel.Allergy
import com.example.resqr.domain.model.medicalRecordModel.EmergencyContact
import com.example.resqr.domain.model.medicalRecordModel.Medication
import com.example.resqr.domain.model.medicalRecordModel.UserMedicalData
import com.example.resqr.domain.model.medicalRecordModel.UserWithMedicalData
import com.example.resqr.domain.model.qrModel.QrResponse
import com.example.resqr.presentation.components.victim.DataSummaryCard
import com.example.resqr.presentation.components.victim.WarningBanner
import androidx.core.graphics.createBitmap
import androidx.navigation.NavController
import com.example.resqr.domain.model.medicalRecordModel.Immunizations
import com.example.resqr.domain.model.medicalRecordModel.MedicalConditions
import com.example.resqr.presentation.components.sharedComponents.TopAppBar
import com.example.resqr.utils.QrForegroundService
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScreen(navController: NavController) {
    val qrViewModel = AppModule.qrViewModel
    val userQrCodeState by qrViewModel.qrState.collectAsState()
    val userWithMedicalData by qrViewModel.userWithMedicalData.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val isServiceRunning by qrViewModel.isServiceRunning.collectAsState()
    val checked by qrViewModel.isNotificationEnabled.collectAsState()
    val file = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "medical_qr_code_${System.currentTimeMillis()}.png"
    )


    LaunchedEffect(userWithMedicalData) {
        userWithMedicalData?.let {
            qrViewModel.getUserQrCode(it)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                showBackButton = true,
                onBackClick = { navController.popBackStack() },
                title = "QR Code",
                actions = {
                    Switch(
                        checked = checked,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                            checkedBorderColor = MaterialTheme.colorScheme.primary,
                            uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                        ),
                        onCheckedChange = { isEnabled ->
                            qrViewModel.setIsNotificationEnabled(isEnabled)
                            if (!isEnabled && isServiceRunning) {
                                context.stopService(
                                    Intent(
                                        context,
                                        QrForegroundService::class.java
                                    )
                                )
                                qrViewModel.setIsServiceRunning(false)
                            }
                        },
                        thumbContent = if (checked) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 8.dp),
        ) {
            when (val state = userQrCodeState) {
                is QrResponse.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is QrResponse.GetQr -> {
                    userWithMedicalData?.let {
                        QRCodeContent(
                            qrBitmap = state.qr,
                            userWithMedicalData = it,
                            onSaveClick = {
                                qrViewModel.saveQRCodeToDevice(state.qr, context)
                                scope.launch {
                                    snackBarHostState.showSnackbar("QR Code saved to gallery.")
                                }
                            },
                            onShareClick = {
                                qrViewModel.shareQRCode(state.qr, context)
                                scope.launch {
                                    snackBarHostState.showSnackbar("Sharing QR code.")
                                }
                            },
                        )
                        if (checked && !isServiceRunning) {
                            try {
                                FileOutputStream(file).use { out ->
                                    state.qr.compress(Bitmap.CompressFormat.PNG, 100, out)
                                }
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    file
                                )
                                val intent =
                                    Intent(context, QrForegroundService::class.java).apply {
                                        putExtra("qrBitmapUri", uri)
                                    }
                                context.startForegroundService(intent)
                                qrViewModel.setIsServiceRunning(true)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Failed to start QR service",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (!checked && isServiceRunning) {
                            context.stopService(Intent(context, QrForegroundService::class.java))
                            qrViewModel.setIsServiceRunning(false)
                        }
                    }
                }

                is QrResponse.QrError -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                QrResponse.Uninitialized -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Preparing QR Code...")
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeContent(
    qrBitmap: Bitmap,
    userWithMedicalData: UserWithMedicalData,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val qrSize = (configuration.screenWidthDp * 0.8f).dp.coerceAtMost(300.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Scan this QR code to share your medical information securely.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "QR code containing your medical information",
                modifier = Modifier
                    .size(qrSize)
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Text(
            text = "Emergency Medical Information",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = userWithMedicalData.user.fullName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        WarningBanner()

        DataSummaryCard(
            bloodType = userWithMedicalData.medicalData.bloodType,
            allergies = userWithMedicalData.medicalData.allergies,
            medications = userWithMedicalData.medicalData.medications,
            emergencyContact = userWithMedicalData.medicalData.emergencyContact[0].phoneNumber
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onSaveClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save QR Code")
            }

            Button(
                onClick = onShareClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share QR Code")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun QRCodeContentPreview() {
    val dummyBitmap = createBitmap(512, 512)

    val dummyUser = UserWithMedicalData(
        user = User(
            fullName = "John Doe",
            email = "william.henry.harrison@example.com",
            phoneNumber = "123-456-7890"
        ),
        medicalData = UserMedicalData(
            bloodType = "O+",
            allergies = listOf(
                Allergy("Peanuts"),
                Allergy("Dust")
            ),
            medications = listOf(
                Medication("Aspirin"),
                Medication("Insulin")
            ),
            emergencyContact = listOf(
                EmergencyContact(
                    name = "Jane Doe",
                    phoneNumber = "123-456-7890"
                )
            ),
            age = "35",
            conditions = listOf(
                MedicalConditions("Hypertension"),
                MedicalConditions("Diabetes")
            ),
            immunizations = listOf(
                Immunizations("Tetanus"),
                Immunizations("COVID-19")
            )
        ),
        userId = 1
    )

    MaterialTheme {
        QRCodeContent(
            qrBitmap = dummyBitmap,
            userWithMedicalData = dummyUser,
            onSaveClick = {},
            onShareClick = {},
        )
    }
}


