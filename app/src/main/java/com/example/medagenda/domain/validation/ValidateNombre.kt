package com.example.medagenda.domain.validation

class ValidateNombre {
    fun execute(nombre: String): ValidationResult {
        if (nombre.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El nombre no puede estar vacío"
            )
        }
        return ValidationResult(successful = true)
    }
}