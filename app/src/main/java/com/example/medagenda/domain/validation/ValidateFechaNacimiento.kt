package com.example.medagenda.domain.validation

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ValidateFechaNacimiento {
    fun execute(fechaNacimiento: String): ValidationResult {
        if (fechaNacimiento.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La fecha de nacimiento no puede estar vacía"
            )
        }

        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.isLenient = false
            val selectedDate = sdf.parse(fechaNacimiento)

            // Check if the selected date is today
            val today = Calendar.getInstance()
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.time = selectedDate

            if (today.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == selectedCalendar.get(Calendar.DAY_OF_YEAR)) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "La fecha de nacimiento no puede ser la fecha actual"
                )
            }
            
            // Check if the date is in the future
            if (selectedDate.after(Date())) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "La fecha de nacimiento no puede ser en el futuro"
                )
            }

        } catch (e: Exception) {
            return ValidationResult(
                successful = false,
                errorMessage = "El formato de la fecha no es válido"
            )
        }

        return ValidationResult(successful = true)
    }
}