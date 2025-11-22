package com.example.medagenda.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL_USUARIOS = "http://10.0.2.2:8081/"
    private const val BASE_URL_CITAS = "http://10.0.2.2:8082/"
    private const val BASE_URL_CONSULTAS = "http://10.0.2.2:8083/"
    private const val BASE_URL_NOTIFICACIONES = "http://10.0.2.2:8084/"

    val usuarios: UsuariosApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_USUARIOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuariosApiService::class.java)
    }

    val citas: CitasApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_CITAS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CitasApiService::class.java)
    }

    val consultas: ConsultasApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_CONSULTAS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConsultasApiService::class.java)
    }

    val notificaciones: NotificacionesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_NOTIFICACIONES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificacionesApiService::class.java)
    }
}