package com.example.medagenda.domain.validation

class ValidateFechaNacimiento {
    fun execute(fechaNacimiento: String): ValidationResult {
        if (fechaNacimiento.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La fecha de nacimiento no puede estar vacía"
            )
        }

        return ValidationResult(successful = true)
    }
}