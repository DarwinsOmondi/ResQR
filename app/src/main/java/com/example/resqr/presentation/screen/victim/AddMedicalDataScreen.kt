package com.example.resqr.presentation.screen.victim

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
import com.example.resqr.presentation.components.sharedComponents.TopAppBar
import com.example.resqr.presentation.components.victim.AddableTextField
import com.example.resqr.presentation.components.victim.LabeledTextField
import com.example.resqr.presentation.components.victim.SectionHeader
import com.example.resqr.presentation.viewmodel.MedicalViewModel
import com.example.resqr.presentation.viewmodel.UserViewModel
import com.example.resqr.ui.theme.VictimTheme


@Composable
fun AddMedicalDataScreen(navController: NavController) {
    val medicalViewModel: MedicalViewModel = AppModule.medicalViewModel
    val userViewModel: UserViewModel = AppModule.userViewModel

    val scrollState = rememberScrollState()
    val snackBarHostState = remember { SnackbarHostState() }

    val medicalState by medicalViewModel.medicalState.collectAsState()
    val userState by userViewModel.userState.collectAsState()


    val user = remember { mutableStateOf<User?>(null) }

    LaunchedEffect(userState) {
        if (userState is UserResponse.GetUser) {
            user.value = (userState as UserResponse.GetUser).user
        }
    }

    val formState = MedicalFormState(
        fullName = medicalViewModel.fullName.collectAsState().value,
        age = medicalViewModel.age.collectAsState().value,
        bloodType = medicalViewModel.bloodType.collectAsState().value,
        emergencyContactName = medicalViewModel.emergencyContactName.collectAsState().value,
        emergencyContactPhone = medicalViewModel.emergencyContactPhone.collectAsState().value,
        allergyInput = medicalViewModel.newAllergy.collectAsState().value,
        medicationInput = medicalViewModel.newMedication.collectAsState().value,
        conditionInput = medicalViewModel.newMedicalCondition.collectAsState().value,
        immunizationsInput = medicalViewModel.newImmunizations.collectAsState().value,
        allergies = medicalViewModel.allergies.collectAsState().value,
        medications = medicalViewModel.medications.collectAsState().value,
        conditions = medicalViewModel.medicalConditions.collectAsState().value,
        immunizations = medicalViewModel.immunizations.collectAsState().value
    )

    val formActions = MedicalFormActions(
        onFullNameChange = { medicalViewModel.onFullNameChange(it) },
        onAgeChange = { medicalViewModel.onAgeChange(it) },
        onBloodTypeChange = { medicalViewModel.onBloodTypeChange(it) },
        onEmergencyContactNameChange = { medicalViewModel.onEmergencyContactNameChange(it) },
        onEmergencyContactPhoneChange = { medicalViewModel.onEmergencyContactPhoneChange(it) },
        onAllergyInputChange = { medicalViewModel.onAllergyInputChange(it) },
        onAddAllergy = { medicalViewModel.onAddAllergy() },
        onRemoveAllergy = { medicalViewModel.onRemoveAllergy(it) },
        onMedicationInputChange = { medicalViewModel.onMedicationInputChange(it) },
        onAddMedication = { medicalViewModel.onAddMedication() },
        onRemoveMedication = { medicalViewModel.onRemoveMedication(it) },
        onConditionChange = { medicalViewModel.onMedicalConditionChange(it) },
        onAddCondition = { medicalViewModel.onAddMedicalCondition() },
        onRemoveCondition = { medicalViewModel.onRemoveMedicalCondition(it) },
        onImmunizationChange = { medicalViewModel.onImmunizationChange(it) },
        onAddImmunization = { medicalViewModel.onAddImmunizations() },
        onRemoveImmunization = { medicalViewModel.onRemoveImmunization(it) },
        onSubmit = {
            user.value?.let { usr ->
                val userMedicalData = UserMedicalData(
                    age = formState.age,
                    bloodType = formState.bloodType,
                    allergies = formState.allergies.map(::Allergy),
                    medications = formState.medications.map(::Medication),
                    conditions = formState.conditions.map(::MedicalConditions),
                    immunizations = formState.immunizations.map(::Immunizations),
                    emergencyContact = listOf(
                        EmergencyContact(
                            formState.emergencyContactName,
                            formState.emergencyContactPhone
                        )
                    )
                )

                val userWithMedicalData = UserWithMedicalData(
                    user = usr,
                    userId = usr.id,
                    medicalData = userMedicalData
                )

                val existingMedicalData =
                    (medicalState as? MedicalResponse.GetMedicalData)?.medicalData
                val isExistingData = existingMedicalData != null

                if (isExistingData) {
                    medicalViewModel.updateMedicalData(userWithMedicalData)
                } else {
                    medicalViewModel.insertMedicalRecord(userWithMedicalData)
                }
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                showBackButton = true,
                onBackClick = { navController.navigate("victim_home_screen") },
                title = "Medicals",
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 8.dp)
        ) {
            when (val state = medicalState) {

                is MedicalResponse.GetMedicalData -> {
                    AddMedicalDataScreenContent(
                        scrollState = scrollState,
                        medicalState = state,
                        formState = formState,
                        formActions = formActions,
                    )

                }


                is MedicalResponse.Uninitialized -> {
                    AddMedicalDataScreenContent(
                        scrollState = scrollState,
                        medicalState = MedicalResponse.Uninitialized,
                        formState = formState,
                        formActions = formActions,
                    )

                }

                is MedicalResponse.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Loading...")
                    }
                }

                is MedicalResponse.MedicalError -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${state.message}")
                    }
                }

                else -> {}
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddMedicalDataScreenContent(
    scrollState: ScrollState,
    medicalState: MedicalResponse,
    formState: MedicalFormState,
    formActions: MedicalFormActions,
) {
    val listOfBloodTypes = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    val isExistingData = medicalState is MedicalResponse.GetMedicalData

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SectionHeader("Basic Information", Icons.Default.Person, MaterialTheme.colorScheme.primary)
        LabeledTextField(
            "Full Name",
            formState.fullName,
            formActions.onFullNameChange,
            Icons.Default.Person
        )
        LabeledTextField("Age", formState.age, formActions.onAgeChange, Icons.Default.CalendarMonth)

        SectionHeader("Blood Type", Icons.Default.Bloodtype, MaterialTheme.colorScheme.primary)
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(listOfBloodTypes) { type ->
                val isSelected = type == formState.bloodType
                Card(
                    onClick = { formActions.onBloodTypeChange(type) },
                    modifier = Modifier.padding(end = 8.dp),
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

        SectionHeader("Emergency Contact", Icons.Default.Phone, MaterialTheme.colorScheme.secondary)
        LabeledTextField(
            "Contact Name",
            formState.emergencyContactName,
            formActions.onEmergencyContactNameChange,
            Icons.Default.People
        )
        LabeledTextField(
            "Phone Number",
            formState.emergencyContactPhone,
            formActions.onEmergencyContactPhoneChange,
            Icons.Default.Phone
        )

        SectionHeader("Immunizations", Icons.Default.Vaccines, MaterialTheme.colorScheme.primary)
        AddableTextField(
            "Add Immunizations",
            formState.immunizationsInput,
            formActions.onImmunizationChange,
            formActions.onAddImmunization
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            formState.immunizations.forEach { imm ->
                AssistChip(
                    onClick = { formActions.onRemoveImmunization(imm) },
                    label = { Text(imm) })
            }
        }

        SectionHeader("Allergies", Icons.Default.Info, MaterialTheme.colorScheme.tertiary)
        AddableTextField(
            "Add Allergy",
            formState.allergyInput,
            formActions.onAllergyInputChange,
            formActions.onAddAllergy
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            formState.allergies.forEach { allergy ->
                AssistChip(
                    onClick = { formActions.onRemoveAllergy(allergy) },
                    label = { Text(allergy) })
            }
        }

        SectionHeader(
            "Current Medications",
            Icons.Default.MedicalServices,
            MaterialTheme.colorScheme.secondary
        )
        AddableTextField(
            "Add Medication",
            formState.medicationInput,
            formActions.onMedicationInputChange,
            formActions.onAddMedication
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            formState.medications.forEach { med ->
                AssistChip(onClick = { formActions.onRemoveMedication(med) }, label = { Text(med) })
            }
        }

        SectionHeader(
            "Medical Conditions",
            Icons.Default.FavoriteBorder,
            MaterialTheme.colorScheme.primary
        )
        AddableTextField(
            "Add Condition",
            formState.conditionInput,
            formActions.onConditionChange,
            formActions.onAddCondition
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            formState.conditions.forEach { cond ->
                AssistChip(
                    onClick = { formActions.onRemoveCondition(cond) },
                    label = { Text(cond) })
            }
        }

        val canSave = formState.fullName.isNotBlank() &&
                formState.age.isNotBlank() &&
                formState.bloodType.isNotBlank()

        Button(
            onClick = { if (canSave) formActions.onSubmit() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isExistingData) "Update" else "Save",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddMedicalDataScreenPreview() {
    val dummyUser = User(
        id = 1,
        fullName = "Jane Doe",
        email = "jane@example.com",
        phoneNumber = "0712345678",
    )

    val dummyState = MedicalFormState(
        fullName = "Jane Doe",
        age = "30",
        bloodType = "O+",
        emergencyContactName = "John Doe",
        emergencyContactPhone = "0798765432",
        allergies = listOf("Peanuts", "Dust"),
        medications = listOf("Paracetamol"),
        conditions = listOf("Asthma"),
        immunizations = listOf("Covid-19", "Tetanus"),
        allergyInput = "",
        medicationInput = "",
        conditionInput = "",
        immunizationsInput = ""
    )

    val dummyActions = MedicalFormActions(
        onFullNameChange = {},
        onAgeChange = {},
        onBloodTypeChange = {},
        onEmergencyContactNameChange = {},
        onEmergencyContactPhoneChange = {},
        onAllergyInputChange = {},
        onAddAllergy = {},
        onRemoveAllergy = {},
        onMedicationInputChange = {},
        onAddMedication = {},
        onRemoveMedication = {},
        onConditionChange = {},
        onAddCondition = {},
        onRemoveCondition = {},
        onImmunizationChange = {},
        onAddImmunization = {},
        onRemoveImmunization = {},
        onSubmit = {},
    )
    val userWithMedicalData = UserWithMedicalData(
        id = 2,
        userId = 2,
        user = dummyUser,
        medicalData = UserMedicalData(
            age = "30",
            bloodType = "O+",
            emergencyContact = listOf(EmergencyContact("John Doe", "0798765432")),
            allergies = listOf(Allergy("Peanuts"), Allergy("Dust")),
            medications = listOf(Medication("Paracetamol")),
            conditions = listOf(MedicalConditions("Asthma")),
            immunizations = listOf(Immunizations("Covid-19"), Immunizations("Tetanus")),
        )
    )
    VictimTheme {
        AddMedicalDataScreenContent(
            scrollState = rememberScrollState(),
            medicalState = MedicalResponse.GetMedicalData(userWithMedicalData),
            formState = dummyState,
            formActions = dummyActions,
        )
    }
}