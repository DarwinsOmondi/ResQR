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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.resqr.model.Allergy
import com.example.resqr.model.Emergency_contact
import com.example.resqr.model.Medication
import com.example.resqr.model.User
import com.example.resqr.model.UserMedicalData
import com.example.resqr.utils.supabaseClient
import io.github.jan.supabase.auth.auth
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(navHostController: NavHostController) {
    val supabaseClient = supabaseClient
    val clientRepository = ClientRepository(supabaseClient)
    val clientViewModel: ClientViewmodel = viewModel(factory = ClientHomeFactory(clientRepository))
    var isSheetOpen by remember { mutableStateOf(false) }
    val currentUser = supabaseClient.auth.currentUserOrNull()
    var username = currentUser?.userMetadata?.get("fullname")
    var useremail = currentUser?.email
    var userphone = currentUser?.userMetadata?.get("phoneNumber")
    var role = currentUser?.userMetadata?.get("role")
    var blood_type by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var allergyList by remember { mutableStateOf(mutableListOf<Allergy>()) }
    var conditions by remember { mutableStateOf("") }
    var showBanner by remember { mutableStateOf(true) }


    var selectedGender by remember { mutableStateOf("male") }
    var blooddropdownstate by remember { mutableStateOf(false) }
    var bloodtypelist = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    var medicationDialog by remember { mutableStateOf(false) }
    var allergyDialog by remember { mutableStateOf(false) }
    var contactDialog by remember { mutableStateOf(false) }
    var contactName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var allergicSubstance by remember { mutableStateOf("") }
    var allergicReaction by remember { mutableStateOf("") }
    var medicationname by remember { mutableStateOf("") }
    var medicationdosage by remember { mutableStateOf("") }
    var medicationfrequency by remember { mutableStateOf("") }
    var medicationFrequencyList = listOf<String>(
        "Daily",
        "Weekly",
        "Monthly",
        "Yearly"
    )
    var medicationFrequecyDropDownState by remember { mutableStateOf(false) }
    var medicationduration by remember { mutableStateOf("") }
    var medicationList by remember { mutableStateOf(mutableListOf<Medication>()) }
    var contactList by remember { mutableStateOf(mutableListOf<Emergency_contact>()) }
    val context = LocalContext.current


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
                        contentDescription = "Update user medical data",
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
                                    onClick = {}
                                )
                                .weight(.5f)
                                .background(Color.Red)
                                .align(Alignment.CenterHorizontally),
                            content = {
                                Text(
                                    "SEND EMERGENCY ALERT",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 32.sp,
                                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                                    )
                                )
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
                                        .align(Alignment.Center), content = {
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
                                                            controller.setZoom(22.0)
                                                            setMultiTouchControls(true)

                                                            val locationOverlay =
                                                                MyLocationNewOverlay(this).apply { enableMyLocation() }
                                                            overlays.add(locationOverlay)
                                                        }
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
                            "Update user medical data",
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
                                    contentDescription = "Blood type"
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        blooddropdownstate = true
                                    },
                                    content = {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = "Dropdown"
                                        )
                                    }
                                )
                            },
                            readOnly = true,
                            label = { Text("Blood Type") }
                        )
                        //BLOOD TYPE DROP DOWN
                        DropdownMenu(
                            expanded = blooddropdownstate,
                            onDismissRequest = {
                                blooddropdownstate = false
                            },
                            content = {
                                bloodtypelist.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it, color = Color.Black) },
                                        onClick = {
                                            blooddropdownstate = false
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
                            label = { Text("Conditions") }
                        )

                        OutlinedButton(
                            onClick = {
                                contactDialog = true
                            }, content = {
                                Text("Add Emergency Contact")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(5.dp)
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
                            content = { Text("Add Medication") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(5.dp)
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
                                                        value = medicationname,
                                                        onValueChange = { medicationname = it },
                                                    )
                                                    OutlinedTextField(
                                                        label = { Text("Medication Dosage") },
                                                        value = medicationdosage,
                                                        onValueChange = { medicationdosage = it },
                                                    )
                                                    OutlinedTextField(
                                                        label = { Text("Medication Frequency") },
                                                        value = medicationfrequency,
                                                        onValueChange = {
                                                            medicationfrequency = it
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
                                                                        medicationfrequency = it
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
                                                            if (medicationname.isNotEmpty() && medicationdosage.isNotEmpty() && medicationfrequency.isNotEmpty() && medicationduration.isNotEmpty()) {
                                                                val newMedication = Medication(
                                                                    name = medicationname,
                                                                    dosage = medicationdosage,
                                                                    frequency = medicationfrequency,
                                                                    duration = medicationduration
                                                                )
                                                                medicationList.add(newMedication)
                                                                medicationDialog = false
                                                                medicationname = ""
                                                                medicationdosage = ""
                                                                medicationfrequency = ""
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
                            content = { Text("Add Allergies") })

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
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            content = {
                                RadioButton(
                                    selected = selectedGender == "male",
                                    onClick = {
                                        gender = "male"
                                    })
                                Text("Male")
                                Spacer(Modifier.width(16.dp))
                                RadioButton(
                                    selected = selectedGender == "female",
                                    onClick = {
                                        gender = "female"
                                    })
                                Text("Female")
                            })
                        val newUser = User(
                            fullname = username.toString(),
                            email = useremail.toString(),
                            phone_number = userphone.toString(),
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
                                clientViewModel.saveClientMedicalData(newUser)
                            },
                            content = { Text("Create your Medical Report") },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(10.dp)
                        )
                    },
                )
            }
        }
    )
}