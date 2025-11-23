package com.example.medagenda.domain.validation

class ValidateEmail {

    private val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()

    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El email no puede estar vacío"
            )
        }
        if (!emailRegex.matches(email)) {
            return ValidationResult(
                successful = false,
                errorMessage = "El formato del email no es válido"
            )
        }
        return ValidationResult(successful = true)
    }
}