package com.example.resqr.domain.model.medicalRecordModel

import kotlinx.serialization.Serializable

@Serializable
data class Allergy(
    val substance: String,
)