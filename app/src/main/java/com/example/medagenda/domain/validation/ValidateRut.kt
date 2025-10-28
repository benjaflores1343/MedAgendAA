package com.example.medagenda.domain.validation

class ValidateRut {
    fun execute(rut: String): ValidationResult {
        if (rut.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El RUT no puede estar vacío"
            )
        }

        // More permissive validation: just check for a plausible format
        val rutPattern = "^(\\d{1,2})\\.?(\\d{3})\\.?(\\d{3})-?([0-9kK])$".toRegex()
        if (!rut.matches(rutPattern)) {
            return ValidationResult(
                successful = false,
                errorMessage = "El formato del RUT no es válido"
            )
        }

        return ValidationResult(successful = true)
    }
}