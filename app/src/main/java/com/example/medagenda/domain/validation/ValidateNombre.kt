package com.example.medagenda.domain.validation

class ValidateNombre {
    fun execute(nombre: String): ValidationResult {
        if (nombre.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El nombre no puede estar vacío"
            )
        }
        val containsOnlyLetters = nombre.all { it.isLetter() || it.isWhitespace() }
        if (!containsOnlyLetters) {
            return ValidationResult(
                successful = false,
                errorMessage = "El nombre solo puede contener letras y espacios"
            )
        }
        return ValidationResult(successful = true)
    }
}