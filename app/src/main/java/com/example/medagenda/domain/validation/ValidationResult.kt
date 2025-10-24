package com.example.medagenda.domain.validation

// Clase para encapsular el resultado de una validación
data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)