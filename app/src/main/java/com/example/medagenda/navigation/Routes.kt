package com.example.medagenda.navigation

sealed class Route(val definition: String) {
    data object Login : Route("login")
    data object Register : Route("register")

    data object Home : Route("home/{userName}/{userRole}/{userId}") {
        fun build(userName: String, userRole: String, userId: Long): String {
            return definition
                .replace("{userName}", userName)
                .replace("{userRole}", userRole)
                .replace("{userId}", userId.toString())
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

    data object MyAppointments : Route("my_appointments/{patientId}") {
        fun build(patientId: Long): String {
            return definition.replace("{patientId}", patientId.toString())
        }
    }

    data object AppointmentDetail : Route("appointment_detail/{citaId}") {
        fun build(citaId: Long): String {
            return definition.replace("{citaId}", citaId.toString())
        }
    }
}