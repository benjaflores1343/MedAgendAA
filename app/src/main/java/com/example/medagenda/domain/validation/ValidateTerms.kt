package com.example.medagenda.domain.validation

class ValidateTerms {
    fun execute(accepted: Boolean): ValidationResult {
        if (!accepted) {
            return ValidationResult(
                successful = false,
                errorMessage = "Debes aceptar los términos y condiciones"
            )
        }
        return ValidationResult(successful = true)
    }
}