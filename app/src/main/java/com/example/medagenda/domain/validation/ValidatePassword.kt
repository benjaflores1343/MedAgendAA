package com.example.medagenda.domain.validation

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe tener al menos 8 caracteres"
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } && password.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe contener letras y números"
            )
        }
        return ValidationResult(successful = true)
    }
}