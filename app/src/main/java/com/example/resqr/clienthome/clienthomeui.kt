package com.example.resqr.clienthome

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.resqr.model.Alert
import com.example.resqr.model.Allergy
import com.example.resqr.model.Emergency_contact
import com.example.resqr.model.Medication
import com.example.resqr.model.User
import com.example.resqr.model.UserMedicalData
import com.example.resqr.utils.LocationService
import com.example.resqr.utils.supabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock.System
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(navHostController: NavHostController) {
    val supabaseClient = supabaseClient
    val clientRepository = ClientRepository(supabaseClient)
    val clientViewModel: ClientViewmodel = viewModel(factory = ClientHomeFactory(clientRepository))
    val uiState by clientViewModel.uiState.collectAsState()
    val timeLeft by clientViewModel.timeLeft.collectAsState()
    val finished by clientViewModel.countdownFinished.collectAsState()
    val countdownFinished by clientViewModel.countdownFinished.collectAsState()
    val hasStarted by clientViewModel.hasStarted.collectAsState()
    var isSheetOpen by remember { mutableStateOf(false) }
    val currentUser = supabaseClient.auth.currentUserOrNull()
    val username = currentUser?.userMetadata?.get("fullname")
    val userEmail = currentUser?.email
    val userPhone = currentUser?.userMetadata?.get("phoneNumber")
    val role = currentUser?.userMetadata?.get("role")
    var blood_type by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var allergyList by remember { mutableStateOf(mutableListOf<Allergy>()) }
    var conditions by remember { mutableStateOf("") }

    val genderOptions = listOf("male", "female", "other")
    var selectedOption by remember { mutableStateOf(genderOptions[0]) }
    var bloodDropDownState by remember { mutableStateOf(false) }
    val bloodTypeList = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    var medicationDialog by remember { mutableStateOf(false) }
    var allergyDialog by remember { mutableStateOf(false) }
    var contactDialog by remember { mutableStateOf(false) }
    var contactName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var allergicSubstance by remember { mutableStateOf("") }
    var allergicReaction by remember { mutableStateOf("") }
    var medicationName by remember { mutableStateOf("") }
    var medicationDosage by remember { mutableStateOf("") }
    var medicationFrequency by remember { mutableStateOf("") }
    val medicationFrequencyList = listOf(
        "Daily",
        "Weekly",
        "Monthly",
        "Yearly"
    )
    var medicationFrequecyDropDownState by remember { mutableStateOf(false) }
    var medicationduration by remember { mutableStateOf("") }
    var medicationList by remember { mutableStateOf(mutableListOf<Medication>()) }
    var contactList by remember { mutableStateOf(mutableListOf<Emergency_contact>()) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    val alertResponse = uiState.alertSuccess
    val context = LocalContext.current
    var mapLocation by remember { mutableStateOf<GeoPoint?>(null) }

    AlertEmergencyListener(context).startListening(clientViewModel)
    var showCard by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        delay(5000) // 5 seconds
        showCard = false
    }

    LaunchedEffect(Unit) {
        clientViewModel.fetchAlertData(currentUser?.id.toString())
        val locationService = LocationService(context)
        val location = locationService.getUserCurrentLocation()
        if (location != null) {
            latitude = location.latitude
            longitude = location.longitude
            mapLocation = GeoPoint(latitude, longitude)
        }
    }
    val alert = Alert(
        alertSentAt = System.now().toString(),
        latitude = latitude,
        longitude = longitude,
        resolved = "false",
        phoneNumber = userPhone.toString(),
    )

    LaunchedEffect(countdownFinished, hasStarted) {
        if (countdownFinished && hasStarted) {
            clientViewModel.sendEmergencyAlert(alert)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Hello! $username") },
                colors = TopAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    scrolledContainerColor = Color.Red
                ),
                actions = {
                    Box(
                        content = {
                            Icon(
                                imageVector = Icons.Default.Person2,
                                contentDescription = "",
                                modifier = Modifier
                            )
                        },
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .clickable(
                                true,
                                onClick = {
                                    navHostController.navigate("profile")
                                }
                            )
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isSheetOpen = true
                },
                content = {
                    Icon(
                        Icons.Default.MedicalInformation,
                        contentDescription = "Create your medical Report",
                        tint = Color.White
                    )
                },
                shape = CircleShape,
                contentColor = MaterialTheme.colorScheme.secondary,
                containerColor = Color.Red
            )
        },
        content = { innerPadding ->
            Box(Modifier.fillMaxSize(), content = {
                Column(
                    Modifier
                        .padding(innerPadding)
                        .background(Color.Red),
                    content = {
                        BannerMessage()
                        Box(
                            Modifier
                                .fillMaxSize()
                                .clickable(
                                    enabled = true,
                                    onClick = {
                                        if (!hasStarted) {
                                            clientViewModel.startCountdown()
                                        }
                                    }
                                )
                                .weight(.5f)
                                .background(Color.Red)
                                .align(Alignment.CenterHorizontally),
                            content = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = when {
                                            !hasStarted -> "SEND EMERGENCY ALERT"
                                            !finished -> "SENDING IN: $timeLeft"
                                            else -> "EMERGENCY ALERT SENT"
                                        },
                                        style = TextStyle(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 32.sp,
                                            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                                        )
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    if (hasStarted && !finished) {
                                        Button(
                                            onClick = {
                                                clientViewModel.stopCountdown()
                                            },
                                            content = { Text("Cancel Alert") },
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                    }
                                }
                            },
                            contentAlignment = Alignment.Center
                        )
                        Box(
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                                .background(Color.White)
                                .align(Alignment.CenterHorizontally),
                            contentAlignment = Alignment.Center,
                            content = {
                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center),
                                    content = {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(.5f),
                                            content = {
                                                AndroidView(
                                                    factory = { context ->
                                                        Configuration.getInstance().userAgentValue =
                                                            context.packageName
                                                        MapView(context).apply {
                                                            setTileSource(TileSourceFactory.MAPNIK)
                                                            controller.setZoom(18.0)
                                                            setMultiTouchControls(true)
                                                            val locationOverlay =
                                                                MyLocationNewOverlay(this).apply {
                                                                    enableMyLocation()
                                                                    enableFollowLocation()
                                                                }
                                                            overlays.add(locationOverlay)
                                                            mapLocation?.let { userLocation ->
                                                                controller.setCenter(userLocation)
                                                            }
                                                        }
                                                    },
                                                    update = { mapView ->
                                                        mapLocation?.let {
                                                            mapView.controller.setCenter(it)
                                                        }
                                                        mapView.invalidate()
                                                    }
                                                )
                                            },
                                            shape = RoundedCornerShape(
                                                topStart = 25.dp,
                                                topEnd = 25.dp,
                                                bottomStart = 25.dp,
                                                bottomEnd = 25.dp
                                            )
                                        )

                                        if (uiState.isLoading) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.align(
                                                    Alignment.CenterHorizontally
                                                )
                                            )
                                        } else if (alertResponse != null) {
                                            val cardColor =
                                                if (alertResponse.resolved == "true") Color(
                                                    0xFF4CAF50
                                                ) else Color(
                                                    0xFFFFC107
                                                )
                                            val message =
                                                if (alertResponse.resolved == "true") "Help is on the way" else "Awaiting response"
                                            val icon =
                                                if (alertResponse.resolved == "true") Icons.Default.CheckCircle else Icons.Default.HourglassTop
                                            if (showCard) {
                                                Card(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 8.dp
                                                        )
                                                        .height(60.dp),
                                                    shape = RoundedCornerShape(12.dp),
                                                    elevation = CardDefaults.cardElevation(6.dp),
                                                    colors = CardDefaults.cardColors(containerColor = cardColor)
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(horizontal = 16.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            imageVector = icon,
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(12.dp))
                                                        Text(
                                                            text = message,
                                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                                color = Color.Black,
                                                                fontWeight = FontWeight.SemiBold
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        } else if (uiState.error != null) {
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = CardDefaults.cardColors(containerColor = Color.Red)
                                            ) {
                                                Text(
                                                    text = "Error: ${uiState.error}",
                                                    modifier = Modifier.padding(16.dp),
                                                    color = Color.White,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                navHostController.navigate("qrcode")
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(32.dp)
                                                .height(60.dp),
                                            shape = RoundedCornerShape(5.dp),
                                            content = {
                                                Text(
                                                    "My QR Code",
                                                    style = TextStyle(color = Color.Black)
                                                )
                                            }
                                        )
                                    },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                )
                            }
                        )
                    }
                )
            })
            if (isSheetOpen) {
                ModalBottomSheet(
                    modifier = Modifier.padding(innerPadding),
                    onDismissRequest = { isSheetOpen = false },
                    sheetState = rememberModalBottomSheetState(),
                    containerColor = Color.White,
                    content = {
                        Text(
                            "Create Your Medical Report",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                            ),
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally),
                        )
                        OutlinedTextField(
                            value = blood_type,
                            onValueChange = { blood_type = it },
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            prefix = {
                                Icon(
                                    Icons.Default.Bloodtype,
                                    contentDescription = "Blood type",
                                    tint = Color.Red
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        bloodDropDownState = true
                                    },
                                    content = {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = "Dropdown",
                                            tint = Color.Blue
                                        )
                                    }
                                )
                            },
                            readOnly = true,
                            label = { Text("Blood Type", color = Color.Gray) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xFF1976D2),
                                unfocusedIndicatorColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                            ),
                        )
                        //BLOOD TYPE DROP DOWN
                        DropdownMenu(
                            expanded = bloodDropDownState,
                            onDismissRequest = {
                                bloodDropDownState = false
                            },
                            content = {
                                bloodTypeList.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it, color = Color.Black) },
                                        onClick = {
                                            bloodDropDownState = false
                                            blood_type = it
                                        },
                                    )
                                }
                            },
                            containerColor = Color.White
                        )

                        OutlinedTextField(
                            value = conditions,
                            onValueChange = { conditions = it },
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            label = { Text("Conditions", color = Color.Gray) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xFF1976D2),
                                unfocusedIndicatorColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                            ),
                        )

                        OutlinedButton(
                            onClick = {
                                contactDialog = true
                            }, content = {
                                Text("Add Emergency Contact", color = Color.Black)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2),
                                contentColor = Color.White
                            )
                        )
                        //Emergency Contact dialog
                        if (contactDialog) {
                            Dialog(
                                onDismissRequest = { contactDialog = false },
                                content = {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.CenterHorizontally),
                                        colors = CardDefaults.cardColors(Color.White),
                                        content = {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp)
                                                    .align(Alignment.CenterHorizontally),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                content = {
                                                    Text(
                                                        "Add Emergency Contacts", style = TextStyle(
                                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                                        )
                                                    )
                                                    OutlinedTextField(
                                                        label = { Text("Contact Name") },
                                                        value = contactName,
                                                        onValueChange = { contactName = it },
                                                    )
                                                    OutlinedTextField(
                                                        label = { Text("Contact Number") },
                                                        value = contactNumber,
                                                        onValueChange = { contactNumber = it },
                                                    )
                                                    Spacer(Modifier.height(16.dp))
                                                    //button to save medication data
                                                    Button(
                                                        onClick = {
                                                            if (contactName.isNotEmpty() && contactNumber.isNotEmpty()) {
                                                                val newContact = Emergency_contact(
                                                                    name = contactName,
                                                                    phone_number = contactNumber
                                                                )
                                                                contactList.add(newContact)
                                                                contactDialog = false
                                                                contactName = ""
                                                                contactNumber = ""
                                                                Log.e("home", newContact.toString())
                                                                Toast.makeText(
                                                                    context,
                                                                    "Emergency contact added to list of emergency contacts",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Please fill in all fields",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        },
                                                        content = { Text("Save Contact") },
                                                        shape = RoundedCornerShape(10.dp),
                                                        modifier = Modifier.fillMaxWidth()
                                                    )

                                                })
                                        }
                                    )
                                }
                            )
                        }
                        OutlinedButton(
                            onClick = { medicationDialog = true },
                            content = { Text("Add Medication", color = Color.Black) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2),
                                contentColor = Color.White
                            )
                        )
                        //Medication dialog
                        if (medicationDialog) {
                            Dialog(
                                onDismissRequest = { medicationDialog = false },
                                content = {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.CenterHorizontally),
                                        colors = CardDefaults.cardColors(Color.White),
                                        content = {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp)
                                                    .align(Alignment.CenterHorizontally),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                content = {
                                                    Text(
                                                        "Add Medications", style = TextStyle(
                                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                                        )
                                                    )
                                                    OutlinedTextField(
                                                        label = { Text("Medication Name") },
                                                        value = medicationName,
                                                        onValueChange = { medicationName = it },
                                                    )
                                                    OutlinedTextField(
                                                        label = { Text("Medication Dosage") },
                                                        value = medicationDosage,
                                                        onValueChange = { medicationDosage = it },
                                                    )
                                                    OutlinedTextField(
                                                        label = { Text("Medication Frequency") },
                                                        value = medicationFrequency,
                                                        onValueChange = {
                                                            medicationFrequency = it
                                                        },
                                                        trailingIcon = {
                                                            IconButton(
                                                                onClick = {
                                                                    medicationFrequecyDropDownState =
                                                                        true

                                                                },
                                                                content = {
                                                                    Icon(
                                                                        Icons.Default.ArrowDropDown,
                                                                        contentDescription = "Dropdown"
                                                                    )

                                                                }
                                                            )
                                                        }
                                                    )
                                                    //Medication frequency dropdown
                                                    DropdownMenu(
                                                        expanded = medicationFrequecyDropDownState,
                                                        onDismissRequest = {
                                                            medicationFrequecyDropDownState = false
                                                        },
                                                        content = {
                                                            medicationFrequencyList.forEach {
                                                                DropdownMenuItem(
                                                                    text = { Text(it) },
                                                                    onClick = {
                                                                        medicationFrequecyDropDownState =
                                                                            false
                                                                        medicationFrequency = it
                                                                    }
                                                                )
                                                            }
                                                        }
                                                    )

                                                    OutlinedTextField(
                                                        label = { Text("Medication Duration") },
                                                        value = medicationduration,
                                                        onValueChange = { medicationduration = it },
                                                    )
                                                    Spacer(Modifier.height(16.dp))
                                                    //button to save medication data
                                                    Button(
                                                        onClick = {
                                                            if (medicationName.isNotEmpty() && medicationDosage.isNotEmpty() && medicationFrequency.isNotEmpty() && medicationduration.isNotEmpty()) {
                                                                val newMedication = Medication(
                                                                    name = medicationName,
                                                                    dosage = medicationDosage,
                                                                    frequency = medicationFrequency,
                                                                    duration = medicationduration
                                                                )
                                                                medicationList.add(newMedication)
                                                                medicationDialog = false
                                                                medicationName = ""
                                                                medicationDosage = ""
                                                                medicationFrequency = ""
                                                                Log.e(
                                                                    "home",
                                                                    newMedication.toString()
                                                                )
                                                                Toast.makeText(
                                                                    context,
                                                                    "Medication added to list of medications",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Please fill in all fields",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        },
                                                        content = { Text("Save Medication") },
                                                        shape = RoundedCornerShape(10.dp),
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                })
                                        }
                                    )
                                }
                            )
                        }

                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(5.dp),
                            onClick = {
                                allergyDialog = true
                            },
                            content = { Text("Add Allergies", color = Color.Black) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2),
                                contentColor = Color.White
                            )
                        )

                        //ALLERGIES DIALOG
                        if (allergyDialog) {
                            Dialog(
                                onDismissRequest = { allergyDialog = false },
                                content = {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.CenterHorizontally),
                                        colors = CardDefaults.cardColors(Color.White),
                                        content = {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp)
                                                    .align(Alignment.CenterHorizontally),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                content = {
                                                    Text(
                                                        "Add Allergies", style = TextStyle(
                                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                                        )
                                                    )
                                                    OutlinedTextField(
                                                        label = { Text("Allergy Substance") },
                                                        value = allergicSubstance,
                                                        onValueChange = { allergicSubstance = it },
                                                    )
                                                    OutlinedTextField(
                                                        label = { Text("Allergic Reaction") },
                                                        value = allergicReaction,
                                                        onValueChange = { allergicReaction = it },
                                                    )
                                                    Spacer(Modifier.height(16.dp))
                                                    //button to save Allergy data
                                                    Button(
                                                        onClick = {
                                                            if (allergicSubstance.isNotEmpty() && allergicReaction.isNotEmpty()) {
                                                                val newAllergy = Allergy(
                                                                    substance = allergicSubstance,
                                                                    reaction = allergicReaction
                                                                )
                                                                allergyList.add(newAllergy)
                                                                allergyDialog = false
                                                                allergicSubstance = ""
                                                                allergicReaction = ""
                                                                Log.e("home", newAllergy.toString())
                                                                Toast.makeText(
                                                                    context,
                                                                    "Allergy added to list of allergies",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Please fill in all fields",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        },
                                                        content = { Text("Save Allergy") },
                                                        shape = RoundedCornerShape(10.dp),
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                })
                                        }
                                    )
                                }
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectableGroup(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            genderOptions.forEach { text ->
                                Row(
                                    modifier = Modifier.selectable(
                                        selected = (text == selectedOption),
                                        onClick = {
                                            selectedOption = text
                                            gender =
                                                text
                                        },
                                        role = Role.RadioButton
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    content = {
                                        RadioButton(
                                            selected = (text == selectedOption),
                                            onClick = {
                                                selectedOption = text
                                                gender = text
                                            },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(0xFF1976D2),
                                            )
                                        )
                                        Text(text)
                                    }
                                )
                            }
                        }
                        val newUser = User(
                            fullname = username.toString(),
                            email = userEmail.toString(),
                            phone_number = userPhone.toString(),
                            role = role.toString(),
                            medicalData = UserMedicalData(
                                blood_type = blood_type,
                                gender = gender,
                                allergies = allergyList,
                                medications = medicationList,
                                conditions = conditions,
                                emergency_contact = contactList,
                            )
                        )
                        Button(
                            onClick = {
                                if (blood_type.isNotEmpty() && gender.isNotEmpty() && conditions.isNotEmpty() && allergyList.isNotEmpty() && medicationList.isNotEmpty() && contactList.isNotEmpty()) {
                                    clientViewModel.saveClientMedicalData(newUser)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please fill in all fields",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            content = { Text("Create your Medical Report") },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF1976D2))
                        )
                    },
                )
            }
        }
    )
}