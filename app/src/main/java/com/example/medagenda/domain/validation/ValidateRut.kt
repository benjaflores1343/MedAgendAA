package com.example.medagenda.domain.validation

class ValidateRut {
    fun execute(rut: String): ValidationResult {
        if (rut.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El RUT no puede estar vacío"
            )
        }

        val cleanRut = rut.replace(Regex("[.\\-]"), "").uppercase()
        if (cleanRut.length < 2) {
            return ValidationResult(successful = false, errorMessage = "Formato de RUT inválido")
        }

        val body = cleanRut.dropLast(1)
        val dv = cleanRut.last()

        if (!body.all { it.isDigit() }) {
            return ValidationResult(successful = false, errorMessage = "El cuerpo del RUT debe ser numérico")
        }

        try {
            val calculatedDv = calculateDv(body.toInt())
            if (dv.toString() != calculatedDv) {
                return ValidationResult(successful = false, errorMessage = "El dígito verificador no es válido")
            }
        } catch (e: NumberFormatException) {
            return ValidationResult(successful = false, errorMessage = "Formato de RUT inválido")
        }

        return ValidationResult(successful = true)
    }

    private fun calculateDv(rut: Int): String {
        var m = 0
        var s = 1
        var t = rut
        while (t != 0) {
            s = (s + t % 10 * (9 - m++ % 6)) % 11
            t /= 10
        }
        return if (s != 0) (s - 1).toString() else "K"
    }
}