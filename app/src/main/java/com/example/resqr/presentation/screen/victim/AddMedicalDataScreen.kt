package com.example.resqr.presentation.screen.victim

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.resqr.di.AppModule
import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.model.medicalRecordModel.Allergy
import com.example.resqr.domain.model.medicalRecordModel.EmergencyContact
import com.example.resqr.domain.model.medicalRecordModel.Immunizations
import com.example.resqr.domain.model.medicalRecordModel.MedicalConditions
import com.example.resqr.domain.model.medicalRecordModel.MedicalResponse
import com.example.resqr.domain.model.medicalRecordModel.Medication
import com.example.resqr.domain.model.medicalRecordModel.UserMedicalData
import com.example.resqr.domain.model.medicalRecordModel.UserWithMedicalData
import com.example.resqr.domain.model.usermodel.UserResponse
import com.example.resqr.presentation.components.victim.AddableTextField
import com.example.resqr.presentation.components.victim.ConfirmDeleteDialog
import com.example.resqr.presentation.components.victim.LabeledTextField
import com.example.resqr.presentation.components.victim.RoundedIcon
import com.example.resqr.presentation.components.victim.SectionHeader
import com.example.resqr.presentation.viewmodel.MedicalViewModel
import com.example.resqr.presentation.viewmodel.UserViewModel
import com.example.resqr.ui.theme.VictimTheme
import kotlinx.coroutines.launch

@Composable
fun AddMedicalDataScreen() {
    val medicalViewModel: MedicalViewModel = AppModule.medicalViewModel
    val userViewModel: UserViewModel = AppModule.userViewModel
    AddMedicalDataScreenContents(medicalViewModel, userViewModel)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddMedicalDataScreenContents(medicalViewModel: MedicalViewModel, userViewModel: UserViewModel) {
    VictimTheme {
        val scrollState = rememberScrollState()
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val medicalState by medicalViewModel.medicalState.collectAsState()
        val userState by userViewModel.userState.collectAsState()
        val fullName by medicalViewModel.fullName.collectAsState()
        val age by medicalViewModel.age.collectAsState()
        val bloodType by medicalViewModel.bloodType.collectAsState()
        val emergencyContactName by medicalViewModel.emergencyContactName.collectAsState()
        val emergencyContactPhone by medicalViewModel.emergencyContactPhone.collectAsState()
        val allergyInput by medicalViewModel.newAllergy.collectAsState()
        val medicationInput by medicalViewModel.newMedication.collectAsState()
        val conditionInput by medicalViewModel.newMedicalCondition.collectAsState()
        val allergies by medicalViewModel.allergies.collectAsState()
        val medications by medicalViewModel.medications.collectAsState()
        val medicalConditions by medicalViewModel.medicalConditions.collectAsState()
        val immunizations by medicalViewModel.immunizations.collectAsState()
        val immunizationsInput by medicalViewModel.newImmunizations.collectAsState()
        val deleteMedicalDataDialog by medicalViewModel.deleteMedicalDataDialog.collectAsState()
        val user = remember { mutableStateOf<User?>(null) }
        LaunchedEffect(Unit) {
            when (userState) {
                is UserResponse.GetUser -> {
                    user.value = (userState as UserResponse.GetUser).user
                    user.value?.id?.let { medicalViewModel.getCurrentUserMedicalDataUseCase(it) }
                }

                else -> {}
            }
        }

        LaunchedEffect(medicalState) {
            when (medicalState) {
                is MedicalResponse.MedicalError -> {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = (medicalState as MedicalResponse.MedicalError).message,
                            actionLabel = "Dismiss",
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is MedicalResponse.MedicalSuccess -> {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = (medicalState as MedicalResponse.MedicalSuccess).message,
                            actionLabel = "Dismiss",
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                else -> {}
            }
        }

        val listOfBloodTypes = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val isExistingData =
            medicalState is MedicalResponse.GetMedicalData && (medicalState as MedicalResponse.GetMedicalData).medicalData != null

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        RoundedIcon(Icons.Default.Person, "Person icon")
                    },
                    title = {
                        Text(
                            "Medical Profile",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    },
                    actions = {
                        Icon(
                            Icons.Default.DeleteForever,
                            contentDescription = "Delete forever",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    medicalViewModel.toggleDeleteMedicalDataDialog()
                                }
                        )
                        if (deleteMedicalDataDialog) {
                            ConfirmDeleteDialog(
                                onConfirm = {
                                    medicalViewModel.deleteMedicalData(user.value!!.id)
                                },
                                onDismiss = {
                                    medicalViewModel.toggleDeleteMedicalDataDialog()
                                }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                SectionHeader(
                    "Basic Information",
                    icon = Icons.Default.Person,
                    tint = MaterialTheme.colorScheme.primary
                )

                LabeledTextField(
                    "Full Name",
                    fullName,
                    medicalViewModel::onFullNameChange,
                    Icons.Default.Person
                )
                LabeledTextField(
                    "Age",
                    age,
                    medicalViewModel::onAgeChange,
                    Icons.Default.CalendarMonth
                )

                SectionHeader(
                    "Blood Type",
                    Icons.Default.Bloodtype,
                    tint = MaterialTheme.colorScheme.primary
                )
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(listOfBloodTypes) { type ->
                        val isSelected = type == bloodType
                        Card(
                            modifier = Modifier.padding(end = 8.dp),
                            onClick = { medicalViewModel.onBloodTypeChange(type) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            ),
                            elevation = CardDefaults.cardElevation(5.dp)
                        ) {
                            Text(
                                type,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                SectionHeader(
                    "Emergency Contact",
                    Icons.Default.Phone,
                    tint = MaterialTheme.colorScheme.secondary
                )
                LabeledTextField(
                    "Contact Name",
                    emergencyContactName,
                    medicalViewModel::onEmergencyContactNameChange,
                    Icons.Default.People
                )
                LabeledTextField(
                    "Phone Number",
                    emergencyContactPhone,
                    medicalViewModel::onEmergencyContactPhoneChange,
                    Icons.Default.Phone
                )
                SectionHeader(
                    "Immunizations",
                    Icons.Default.Vaccines,
                    tint = MaterialTheme.colorScheme.primary
                )
                AddableTextField(
                    "Add immunizations",
                    immunizationsInput,
                    medicalViewModel::onImmunizationChange,
                    medicalViewModel::onAddImmunizations
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    immunizations.forEach { imm ->
                        AssistChip(onClick = {
                            medicalViewModel.onRemoveImmunization(imm)
                        }, label = { Text(imm) })
                    }
                }
                SectionHeader(
                    "Allergies",
                    Icons.Default.Info,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                AddableTextField(
                    "Add Allergy",
                    allergyInput,
                    medicalViewModel::onAllergyInputChange,
                    medicalViewModel::onAddAllergy
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    allergies.forEach { allergy ->
                        AssistChip(onClick = {
                            medicalViewModel.onRemoveAllergy(allergy)
                        }, label = { Text(allergy) })
                    }
                }

                SectionHeader(
                    "Current Medications",
                    Icons.Default.MedicalServices,
                    tint = MaterialTheme.colorScheme.secondary
                )
                AddableTextField(
                    "Add Medication",
                    medicationInput,
                    medicalViewModel::onMedicationInputChange,
                    medicalViewModel::onAddMedication
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    medications.forEach { med ->
                        AssistChip(onClick = {
                            medicalViewModel.onRemoveMedication(med)
                        }, label = { Text(med) })
                    }
                }

                SectionHeader(
                    "Medical Conditions",
                    Icons.Default.FavoriteBorder,
                    tint = MaterialTheme.colorScheme.primary
                )
                AddableTextField(
                    "Add medical condition",
                    conditionInput,
                    medicalViewModel::onMedicalConditionChange,
                    medicalViewModel::onAddMedicalCondition
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    medicalConditions.forEach { cond ->
                        AssistChip(onClick = {
                            medicalViewModel.onRemoveMedicalCondition(cond)
                        }, label = { Text(cond) })
                    }
                }
                val isUserLoaded = user.value != null
                val canSave =
                    isUserLoaded && fullName.isNotBlank() && age.isNotBlank() && bloodType.isNotBlank()
                val userMedicalData = UserMedicalData(
                    age = age,
                    bloodType = bloodType,
                    allergies = allergies.map { Allergy(it) },
                    medications = medications.map { Medication(it) },
                    conditions = medicalConditions.map { MedicalConditions(it) },
                    immunizations = immunizations.map { Immunizations(it) },
                    emergencyContact = listOf(
                        EmergencyContact(
                            emergencyContactName,
                            emergencyContactPhone
                        )
                    )
                )

                val userWithMedicalData = user.value?.let {
                    UserWithMedicalData(
                        user = it,
                        userId = user.value!!.id,
                        medicalData = userMedicalData
                    )
                }
                Button(
                    onClick = {
                        if (userWithMedicalData != null && canSave) {
                            if (isExistingData) {
                                medicalViewModel.updateMedicalData(userWithMedicalData)
                            } else {
                                medicalViewModel.insertMedicalRecord(userWithMedicalData)
                            }
                        } else {
                            println("User or medical data is null")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (medicalState is MedicalResponse.Loading) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Saving...", style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Save, contentDescription = "Save medical data")
                            Text(
                                if (isExistingData) "Update" else "Save",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}