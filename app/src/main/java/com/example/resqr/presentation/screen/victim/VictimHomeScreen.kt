package com.example.resqr.presentation.screen.victim

import android.location.Geocoder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.resqr.di.AppModule
import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.model.medicalRecordModel.EmergencyContact
import com.example.resqr.domain.model.medicalRecordModel.MedicalResponse
import com.example.resqr.domain.model.medicalRecordModel.UserWithMedicalData
import com.example.resqr.domain.model.usermodel.UserResponse
import com.example.resqr.presentation.components.both.ActionDisplayerCards
import com.example.resqr.presentation.components.both.BottomNavBar
import com.example.resqr.presentation.components.victim.EmergencyAlertDialogWrapper
import com.example.resqr.presentation.components.victim.QuickActionCardVictim
import com.example.resqr.presentation.components.victim.VoiceCommandsCard
import com.example.resqr.presentation.viewmodel.AlertViewModel
import com.example.resqr.presentation.viewmodel.MedicalViewModel
import com.example.resqr.presentation.viewmodel.QrViewModel
import com.example.resqr.presentation.viewmodel.UserViewModel
import com.example.resqr.utils.LocationService

@Composable
fun VictimHomeScreen(navController: NavController) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = AppModule.userViewModel
    val alertViewModel: AlertViewModel = AppModule.alertViewModel
    val medicalViewModel: MedicalViewModel = AppModule.medicalViewModel
    val qrViewModel: QrViewModel = AppModule.qrViewModel

    val userState by userViewModel.userState.collectAsState()
    val alertState by alertViewModel.alertState.collectAsState()
    val medicalState by medicalViewModel.medicalState.collectAsState()
    val showAlertDialog by userViewModel.showAlert.collectAsState()
    val timeLeft by alertViewModel.timeLeft.collectAsState()
    val countdownFinished by alertViewModel.countdownFinished.collectAsState()
    val hasStarted by alertViewModel.hasStarted.collectAsState()
    val longitude by alertViewModel.longitude.collectAsState()
    val latitude by alertViewModel.latitude.collectAsState()
    val victimLocation by alertViewModel.victimLocation.collectAsState()

    var currentUser by remember { mutableStateOf<User?>(null) }
    var userMedicalData by remember { mutableStateOf<UserWithMedicalData?>(null) }
    val userContact = userMedicalData?.medicalData?.emergencyContact?.firstOrNull()

    LaunchedEffect(Unit) {
        userViewModel.getUser()
    }

    LaunchedEffect(userState) {
        currentUser = (userState as? UserResponse.GetUser)?.user
        currentUser?.let {
            medicalViewModel.getCurrentUserMedicalDataUseCase(it.id)
        }
    }

    LaunchedEffect(medicalState) {
        userMedicalData = (medicalState as? MedicalResponse.GetMedicalData)?.medicalData
        qrViewModel.populateUserWithMedicalData(userMedicalData)
        medicalViewModel.populateFormFields(userMedicalData)
    }

    LaunchedEffect(Unit) {
        val locationService = LocationService(context)
        locationService.getUserCurrentLocation()?.let {
            alertViewModel.updateLocation(it.latitude, it.longitude)
            val decoded = Geocoder(context).getFromLocation(it.latitude, it.longitude, 1)
            alertViewModel.updateVictimLocation(
                decoded?.getOrNull(0)?.getAddressLine(0)?.substringAfter(", ")?.trim()
                    ?: "Unknown Location"
            )
        }
    }

    LaunchedEffect(countdownFinished) {
        if (countdownFinished && currentUser != null && userMedicalData != null && userContact != null) {
            alertViewModel.triggerSendAlert(
                user = currentUser!!,
                contact = EmergencyContact(userContact.name, userContact.phoneNumber),
                latitude = latitude.toString(),
                longitude = longitude.toString()
            )
        }
    }

    LaunchedEffect(showAlertDialog) {
        if (showAlertDialog && !hasStarted) {
            alertViewModel.startCountdown()
        }
    }

    VictimHomeScreenContent(
        navController = navController,
        userState = userState,
        showAlertDialog = showAlertDialog,
        timeLeft = timeLeft,
        hasStarted = hasStarted,
        countdownFinished = countdownFinished,
        victimLocation = victimLocation,
        onToggleDialog = userViewModel::toggleAlertDialog,
        onStopCountdown = alertViewModel::stopCountdownEarly,
        onStartCountdown = alertViewModel::startCountdown
    )
}


@Composable
fun VictimHomeScreenContent(
    navController: NavController,
    userState: UserResponse,
    showAlertDialog: Boolean,
    timeLeft: Int,
    hasStarted: Boolean,
    countdownFinished: Boolean,
    victimLocation: String?,
    onToggleDialog: () -> Unit,
    onStopCountdown: () -> Unit,
    onStartCountdown: () -> Unit,
) {
    val actions = remember {
        listOf(
            Triple(Icons.Default.Person, "Medical Profile", "Medical Profile"),
            Triple(Icons.Default.QrCode, "Share QR Code", "Share QR Code"),
            Triple(Icons.Default.Phone, "Call 911", "Call 911"),
            Triple(Icons.Default.LocationOn, "Update Location", "Update Location")
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionDisplayerCards(
                icon = Icons.Default.Favorite,
                contentDescription = "Person Icon",
                title = "Emergency Assistant",
                content = "Tap to speak for",
                iconColor = Color.White,
                cardColor = Color.Red,
                onCardClick = {}
            )
            ActionDisplayerCards(
                icon = Icons.Default.WarningAmber,
                contentDescription = "Emergency Icon",
                title = "Emergency",
                content = "Tap to initiate",
                iconColor = Color.White,
                cardColor = Color.Red,
                onCardClick = {
                    onToggleDialog()
                    onStartCountdown()
                }
            )
            Text(
                "Quick Actions",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(actions.size) { index ->
                    val action = actions[index]
                    QuickActionCardVictim(
                        icon = action.first,
                        contentDescription = action.second,
                        title = action.third,
                        iconColor = Color(0xFF1976D2),
                        cardColor = Color.White,
                        onCardClick = {
                            when (action.third) {
                                "Medical Profile" -> navController.navigate(Routes.MEDICAL_PROFILE)
                                "Share QR Code" -> navController.navigate(Routes.SHARE_QR)
                                "Call 911" -> navController.navigate(Routes.CALL_911)
                                "Update Location" -> navController.navigate(Routes.UPDATE_LOCATION)
                            }
                        }
                    )
                }
            }

            VoiceCommandsCard(
                onVoiceCommandClick = {}
            )

            EmergencyAlertDialogWrapper(
                showDialog = showAlertDialog,
                onDismissRequest = {
                    onStopCountdown()
                    onToggleDialog()
                },
                onCall911 = {
                    onStopCountdown()
                    onToggleDialog()
                    navController.navigate(Routes.CALL_911)
                },
                onCancelAlert = {
                    onStopCountdown()
                    onToggleDialog()
                },
                remainingTime = timeLeft,
                location = victimLocation ?: "Unknown Location"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VictimHomeScreenContentPreview() {
    VictimHomeScreenContent(
        navController = rememberNavController(),
        userState = UserResponse.Loading,
        showAlertDialog = false,
        timeLeft = 30,
        hasStarted = false,
        countdownFinished = false,
        victimLocation = "Nairobi, Kenya",
        onToggleDialog = {},
        onStopCountdown = {},
        onStartCountdown = {}
    )
}


object Routes {
    const val MEDICAL_PROFILE = "add_medical_data"
    const val SHARE_QR = "show_qr"
    const val CALL_911 = "call_911"
    const val UPDATE_LOCATION = "update_location"
}
