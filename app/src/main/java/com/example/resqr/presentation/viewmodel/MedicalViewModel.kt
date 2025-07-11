package com.example.resqr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.domain.model.medicalRecordModel.MedicalResponse
import com.example.resqr.domain.model.medicalRecordModel.UserWithMedicalData
import com.example.resqr.domain.usecase.medical.MedicalUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MedicalViewModel(private val medicalUseCases: MedicalUseCases) : ViewModel() {
    private val _medicalState = MutableStateFlow<MedicalResponse>(MedicalResponse.Uninitialized)
    val medicalState: StateFlow<MedicalResponse> = _medicalState

    private val _medicalData = MutableStateFlow<UserWithMedicalData?>(null)
    val medicalData: StateFlow<UserWithMedicalData?> = _medicalData

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _age = MutableStateFlow("")
    val age: StateFlow<String> = _age

    private val _bloodType = MutableStateFlow("")
    val bloodType: StateFlow<String> = _bloodType

    private val _allergies = MutableStateFlow<List<String>>(emptyList())
    val allergies: StateFlow<List<String>> = _allergies

    private val _medications = MutableStateFlow<List<String>>(emptyList())
    val medications: StateFlow<List<String>> = _medications

    private val _medicalConditions = MutableStateFlow<List<String>>(emptyList())
    val medicalConditions: StateFlow<List<String>> = _medicalConditions

    private val _immunizations = MutableStateFlow<List<String>>(emptyList())
    val immunizations: StateFlow<List<String>> = _immunizations

    private val _emergencyContactName = MutableStateFlow("")
    val emergencyContactName: StateFlow<String> = _emergencyContactName

    private val _emergencyContactPhone = MutableStateFlow("")
    val emergencyContactPhone: StateFlow<String> = _emergencyContactPhone

    private val _newAllergy = MutableStateFlow("")
    val newAllergy: StateFlow<String> = _newAllergy

    private val _newImmunization = MutableStateFlow("")
    val newImmunizations: StateFlow<String> = _newImmunization

    private val _newMedication = MutableStateFlow("")
    val newMedication: StateFlow<String> = _newMedication

    private val _newMedicalCondition = MutableStateFlow("")
    val newMedicalCondition: StateFlow<String> = _newMedicalCondition

    fun onFullNameChange(value: String) {
        _fullName.value = value
    }

    fun onAgeChange(value: String) {
        _age.value = value
    }

    fun onBloodTypeChange(value: String) {
        _bloodType.value = value
    }

    fun onEmergencyContactNameChange(value: String) {
        _emergencyContactName.value = value
    }

    fun onEmergencyContactPhoneChange(value: String) {
        _emergencyContactPhone.value = value
    }

    fun onAllergyInputChange(value: String) {
        _newAllergy.value = value
    }

    fun onMedicalConditionChange(value: String) {
        _newMedicalCondition.value = value
    }

    fun onImmunizationChange(value: String) {
        _newImmunization.value = value
    }

    fun onMedicationInputChange(value: String) {
        _newMedication.value = value
    }

    fun onAddAllergy() {
        if (_newAllergy.value.isNotBlank()) {
            _allergies.value = _allergies.value + _newAllergy.value
            _newAllergy.value = ""
        }
    }

    fun onAddImmunizations() {
        if (_newImmunization.value.isNotBlank()) {
            _immunizations.value = _immunizations.value + _newImmunization.value
            _newImmunization.value = ""
        }
    }

    fun onAddMedication() {
        if (_newMedication.value.isNotBlank()) {
            _medications.value = _medications.value + _newMedication.value
            _newMedication.value = ""
        }
    }

    fun onAddMedicalCondition() {
        if (_newMedicalCondition.value.isNotBlank()) {
            _medicalConditions.value = _medicalConditions.value + _newMedicalCondition.value
            _newMedicalCondition.value = ""
        }
    }

    fun onRemoveMedication(med: String) {
        _medications.value = _medications.value.toMutableList().apply {
            remove(med)
        }
    }

    fun onRemoveAllergy(allergy: String) {
        _allergies.value = _allergies.value.toMutableList().apply {
            remove(allergy)
        }
    }

    fun onRemoveMedicalCondition(condition: String) {
        _medicalConditions.value = _medicalConditions.value.toMutableList().apply {
            remove(condition)
        }
    }

    fun onRemoveImmunization(immunization: String) {
        _immunizations.value = _immunizations.value.toMutableList().apply {
            remove(immunization)
        }
    }

    private val _deleteMedicalDataDialog = MutableStateFlow(false)
    val deleteMedicalDataDialog: StateFlow<Boolean> = _deleteMedicalDataDialog

    fun toggleDeleteMedicalDataDialog() {
        _deleteMedicalDataDialog.value = !_deleteMedicalDataDialog.value
    }


    fun insertMedicalRecord(userWithMedicalData: UserWithMedicalData) {
        viewModelScope.launch {
            _medicalState.value = MedicalResponse.Loading
            medicalUseCases.insertMedicalData(userWithMedicalData).collect { result ->
                _medicalState.value = result
            }
        }
    }

    fun getCurrentUserMedicalDataUseCase(id: Int) {
        viewModelScope.launch {
            _medicalState.value = MedicalResponse.Loading
            medicalUseCases.getCurrentUserMedicalData(id).collect { result ->
                _medicalState.value = result
                if (result is MedicalResponse.GetMedicalData && result.medicalData != null) {
                    populateFormFields(result.medicalData)
                }
            }
        }
    }

    fun getMedicalData() {
        viewModelScope.launch {
            _medicalState.value = MedicalResponse.Loading
            medicalUseCases.getMedicalData().collect { result ->
                _medicalState.value = result
            }
        }
    }

    fun deleteMedicalData(id: String) {
        viewModelScope.launch {
            _medicalState.value = MedicalResponse.Loading
            medicalUseCases.deleteMedicalData(id).collect { result ->
                _medicalState.value = result
            }
        }
    }

    fun updateMedicalData(userWithMedicalData: UserWithMedicalData) {
        viewModelScope.launch {
            _medicalState.value = MedicalResponse.Loading
            medicalUseCases.updateMedicalData(userWithMedicalData).collect { result ->
                _medicalState.value = result
            }
        }
    }

    fun populateFormFields(medicalData: UserWithMedicalData?) {
        if (medicalData != null) {
            _fullName.value = medicalData.user.fullName
            _age.value = medicalData.medicalData.age
            _bloodType.value = medicalData.medicalData.bloodType
            _emergencyContactName.value =
                medicalData.medicalData.emergencyContact.firstOrNull()?.name ?: ""
            _emergencyContactPhone.value =
                medicalData.medicalData.emergencyContact.firstOrNull()?.phoneNumber ?: ""
            _allergies.value = medicalData.medicalData.allergies.map { it.substance }
            _medications.value = medicalData.medicalData.medications.map { it.name }
            _medicalConditions.value = medicalData.medicalData.conditions.map { it.condition }
            _immunizations.value = medicalData.medicalData.immunizations.map { it.name }
        }
    }
}