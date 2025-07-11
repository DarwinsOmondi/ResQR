package com.example.resqr.presentation.screen.qr

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.resqr.domain.model.medicalRecordModel.Immunizations
import com.example.resqr.domain.model.medicalRecordModel.MedicalConditions

@Composable
fun QRScreen() {
    val qrViewModel = AppModule.qrViewModel
    val userQrCodeState by qrViewModel.qrState.collectAsState()
    val userWithMedicalData by qrViewModel.userWithMedicalData.collectAsState()
    Log.d("QRScreen", "userWithMedicalData: $userWithMedicalData")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isServiceRunning by remember { mutableStateOf(false) }

    LaunchedEffect(userWithMedicalData) {
        userWithMedicalData?.let {
            qrViewModel.getUserQrCode(it)
        }
    }

    when (userQrCodeState) {
        is QrResponse.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is QrResponse.GetQr -> {
            val qrBitmap = (userQrCodeState as QrResponse.GetQr).qr

            userWithMedicalData?.let {
                QRCodeContent(
                    qrBitmap = qrBitmap,
                    userWithMedicalData = it,
                    onSaveClick = {
                        qrViewModel.saveQRCodeToDevice(qrBitmap, context)
                    },
                    onShareClick = {
                        qrViewModel.shareQRCode(qrBitmap, context)
                    },
                    onToggleService = {
                        isServiceRunning = !isServiceRunning
                        // handle lock screen service here
                    },
                    isServiceRunning = isServiceRunning
                )
            }
        }

        is QrResponse.QrError -> {
            val errorMsg = (userQrCodeState as QrResponse.QrError).message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: $errorMsg", color = MaterialTheme.colorScheme.error)
            }
        }

        QrResponse.Uninitialized -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Preparing QR Code...")
            }
        }
    }
}

@Composable
fun QRCodeContent(
    qrBitmap: Bitmap,
    userWithMedicalData: UserWithMedicalData,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    onToggleService: () -> Unit,
    isServiceRunning: Boolean
) {
    val configuration = LocalConfiguration.current
    val qrSize = (configuration.screenWidthDp * 0.8f).dp.coerceAtMost(300.dp)

    Column(
        modifier = Modifier
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
        Text(
            "Emergency Medical Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.semantics { contentDescription = "User Information Title" },
            textAlign = TextAlign.Center
        )
        Text(
            userWithMedicalData.user.fullName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .semantics { contentDescription = "User Information Title" }
        )
        Spacer(modifier = Modifier.height(8.dp))
        WarningBanner()
        Spacer(modifier = Modifier.height(8.dp))
        DataSummaryCard(
            userWithMedicalData.medicalData.bloodType,
            allergies = userWithMedicalData.medicalData.allergies,
            medications = userWithMedicalData.medicalData.medications,
            emergencyContact = userWithMedicalData.medicalData.emergencyContact[0].phoneNumber,
        )
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
            onToggleService = {},
            isServiceRunning = false
        )
    }
}


