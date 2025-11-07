package com.example.medagenda.domain.validation

class ValidateApellido {
    fun execute(apellido: String): ValidationResult {
        if (apellido.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El apellido no puede estar vacío"
            )
        }
        val containsOnlyLetters = apellido.all { it.isLetter() || it.isWhitespace() }
        if (!containsOnlyLetters) {
            return ValidationResult(
                successful = false,
                errorMessage = "El apellido solo puede contener letras y espacios"
            )
        }
        return ValidationResult(successful = true)
    }
}