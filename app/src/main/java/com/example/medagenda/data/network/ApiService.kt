package com.example.medagenda.data.network

import com.example.medagenda.data.network.LoginRequest
import com.example.medagenda.data.network.UsuarioApi
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuariosApiService {
    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): UsuarioApi
}

interface CitasApiService {

}

interface ConsultasApiService {

}

interface NotificacionesApiService {

}
