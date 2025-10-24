package com.example.medagenda.domain.validation

class ValidateTelefono {
    fun execute(telefono: String): ValidationResult {
        if (telefono.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El teléfono no puede estar vacío"
            )
        }
        if (!telefono.all { it.isDigit() || it == '+' }) {
             return ValidationResult(
                successful = false,
                errorMessage = "El teléfono solo puede contener números y el símbolo '+'"
            )
        }
        if (telefono.length < 9) {
            return ValidationResult(
                successful = false,
                errorMessage = "El teléfono debe tener al menos 9 dígitos"
            )
        }
        return ValidationResult(successful = true)
    }
}