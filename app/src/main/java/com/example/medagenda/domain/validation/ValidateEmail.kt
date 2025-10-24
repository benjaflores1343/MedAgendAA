package com.example.medagenda.domain.validation

class ValidateEmail {

    private val EMAIL_REGEX = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"
    )

    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El email no puede estar vacío"
            )
        }
        if (!EMAIL_REGEX.matches(email)) {
            return ValidationResult(
                successful = false,
                errorMessage = "El formato del email no es válido"
            )
        }
        return ValidationResult(successful = true)
    }
}