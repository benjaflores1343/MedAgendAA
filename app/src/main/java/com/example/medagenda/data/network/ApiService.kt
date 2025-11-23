package com.example.medagenda.data.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("citas/medico/{id}")
    suspend fun getAppointmentsForDoctor(@Path("id") medicoId: Long): List<DoctorAppointmentApiResponse>

    @PUT("citas/{id}/estado")
    suspend fun updateAppointmentStatus(@Path("id") citaId: Long, @Body statusRequest: UpdateAppointmentStatusRequest)

    @GET("citas/{id}")
    suspend fun getAppointmentDetails(@Path("id") citaId: Long): AppointmentDetailApiResponse
}

interface ConsultasApiService {
    @GET("recetas/paciente/{id}")
    suspend fun getRecetas(@Path("id") patientId: Long): List<RecetaApiResponse>

    @POST("recetas")
    suspend fun createReceta(@Body createRecetaRequest: CreateRecetaRequest)

    @HTTP(method = "DELETE", path = "recetas", hasBody = true)
    suspend fun deleteRecetas(@Body deleteRecetasRequest: DeleteRecetasRequest)
}

interface NotificacionesApiService {

}
