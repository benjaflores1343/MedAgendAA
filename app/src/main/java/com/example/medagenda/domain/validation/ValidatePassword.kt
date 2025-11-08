package com.example.medagenda.domain.validation

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if (password.length < 6) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe tener al menos 6 caracteres"
            )
        }
        val containsNumber = password.any { it.isDigit() }
        if (!containsNumber) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe contener al menos un número"
            )
        }
        return ValidationResult(successful = true)
    }
}