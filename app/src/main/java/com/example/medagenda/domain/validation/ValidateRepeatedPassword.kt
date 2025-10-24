package com.example.medagenda.domain.validation

class ValidateRepeatedPassword {
    fun execute(password: String, repeatedPassword: String): ValidationResult {
        if (password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "Las contraseñas no coinciden"
            )
        }
        return ValidationResult(successful = true)
    }
}