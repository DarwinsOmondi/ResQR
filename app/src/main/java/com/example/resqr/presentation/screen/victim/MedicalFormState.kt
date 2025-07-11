package com.example.resqr.presentation.screen.victim

data class MedicalFormState(
    val fullName: String = "",
    val age: String = "",
    val bloodType: String = "",
    val emergencyContactName: String = "",
    val emergencyContactPhone: String = "",
    val allergyInput: String = "",
    val medicationInput: String = "",
    val conditionInput: String = "",
    val immunizationsInput: String = "",
    val allergies: List<String> = emptyList(),
    val medications: List<String> = emptyList(),
    val conditions: List<String> = emptyList(),
    val immunizations: List<String> = emptyList()
)