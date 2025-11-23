package com.example.medagenda.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UsuariosApiService {
    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): UserApiResponse

    @POST("usuarios/register")
    suspend fun register(@Body registerRequest: RegisterRequest)
}

interface CitasApiService {
    @GET("especialidades")
    suspend fun getEspecialidades(): List<EspecialidadApi>

    @GET("medicos/especialidad/{id}")
    suspend fun getMedicosPorEspecialidad(@Path("id") especialidadId: Long): List<MedicoApi>

    @GET("horarios/disponibles/{idMedico}")
    suspend fun getHorariosDisponibles(@Path("idMedico") medicoId: Long): List<HorarioApi>

    @POST("citas")
    suspend fun createAppointment(@Body createAppointmentRequest: CreateAppointmentRequest)

    @GET("citas/paciente/{id}")
    suspend fun getAppointmentsForPatient(@Path("id") patientId: Long): List<AppointmentApiResponse>
}

interface ConsultasApiService {

}

interface NotificacionesApiService {

}
