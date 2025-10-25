package com.example.medagenda.navigation

sealed class Route(val definition: String) {
    data object Login : Route("login")
    data object Register : Route("register")

    data object Home : Route("home/{userName}/{userRole}/{pacienteId}") {
        fun build(userName: String, userRole: String, pacienteId: Long): String {
            return definition
                .replace("{userName}", userName)
                .replace("{userRole}", userRole)
                .replace("{pacienteId}", pacienteId.toString())
        }
    }

    data object RequestAppointment : Route("request_appointment/{pacienteId}") {
        fun build(pacienteId: Long): String {
            return definition.replace("{pacienteId}", pacienteId.toString())
        }
    }

    data object SelectDoctor : Route("select_doctor/{specialtyId}/{pacienteId}") {
        fun build(specialtyId: Long, pacienteId: Long): String {
            return definition
                .replace("{specialtyId}", specialtyId.toString())
                .replace("{pacienteId}", pacienteId.toString())
        }
    }

    data object SelectTimeSlot : Route("select_time_slot/{medicoId}/{pacienteId}") {
        fun build(medicoId: Long, pacienteId: Long): String {
            return definition
                .replace("{medicoId}", medicoId.toString())
                .replace("{pacienteId}", pacienteId.toString())
        }
    }
}