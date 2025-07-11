package com.example.resqr.presentation.screen.victim

data class MedicalFormActions(
    val onFullNameChange: (String) -> Unit,
    val onAgeChange: (String) -> Unit,
    val onBloodTypeChange: (String) -> Unit,
    val onEmergencyContactNameChange: (String) -> Unit,
    val onEmergencyContactPhoneChange: (String) -> Unit,
    val onAllergyInputChange: (String) -> Unit,
    val onAddAllergy: () -> Unit,
    val onRemoveAllergy: (String) -> Unit,
    val onMedicationInputChange: (String) -> Unit,
    val onAddMedication: () -> Unit,
    val onRemoveMedication: (String) -> Unit,
    val onConditionChange: (String) -> Unit,
    val onAddCondition: () -> Unit,
    val onRemoveCondition: (String) -> Unit,
    val onImmunizationChange: (String) -> Unit,
    val onAddImmunization: () -> Unit,
    val onRemoveImmunization: (String) -> Unit,
    val onSubmit: () -> Unit
)
