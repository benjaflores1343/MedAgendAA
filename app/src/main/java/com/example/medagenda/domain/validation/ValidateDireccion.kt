package com.example.medagenda.domain.validation

class ValidateDireccion {
    fun execute(direccion: String): ValidationResult {
        if (direccion.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La dirección no puede estar vacía"
            )
        }
        return ValidationResult(successful = true)
    }
}