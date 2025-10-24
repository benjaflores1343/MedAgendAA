package com.example.medagenda.domain.validation

class ValidateApellido {
    fun execute(apellido: String): ValidationResult {
        if (apellido.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El apellido no puede estar vacío"
            )
        }
        return ValidationResult(successful = true)
    }
}