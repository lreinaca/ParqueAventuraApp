package com.eam.parqueaventuraapp.data.network

import com.eam.parqueaventuraapp.data.modelo.Atraccion
import retrofit2.http.*

interface AtraccionApiService {
    @GET("atracciones")
    suspend fun obtenerAtracciones(): List<Atraccion>

    @POST("atracciones")
    suspend fun crearAtraccion(@Body atraccion: Atraccion): Atraccion

    @PUT("atracciones/{id}")
    suspend fun actualizarAtraccion(@Path("id") id: String, @Body atraccion: Atraccion): Atraccion

    @DELETE("atracciones/{id}")
    suspend fun eliminarAtraccion(@Path("id") id: String)
}
