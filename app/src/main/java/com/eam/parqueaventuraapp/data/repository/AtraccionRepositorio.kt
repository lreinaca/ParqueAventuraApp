package com.eam.parqueaventuraapp.data.repository

import com.eam.parqueaventuraapp.data.modelo.Atraccion
import com.eam.parqueaventuraapp.data.network.AtraccionApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AtraccionRepositorio(private val apiService: AtraccionApiService) {

    private val _atracciones = MutableStateFlow<List<Atraccion>>(emptyList())
    val todasLasAtracciones: Flow<List<Atraccion>> = _atracciones.asStateFlow()

    suspend fun refrescarAtracciones() {
        try {
            val lista = apiService.obtenerAtracciones()
            _atracciones.value = lista
        } catch (e: java.io.IOException) {
            // Sin internet → lanza un error con mensaje claro
            throw Exception("Sin conexión a internet")
        } catch (e: retrofit2.HttpException) {
            // El servidor falló (ej: error 500) → lanza un error con mensaje claro
            throw Exception("Error del servidor: ${e.code()}")
        }
    }

    suspend fun insertar(atraccion: Atraccion) {
        try {
            apiService.crearAtraccion(atraccion)  // envía la nueva atracción al servidor
            refrescarAtracciones()  // actualiza la lista local
        } catch (e: java.io.IOException) {
            throw Exception("Sin conexión a internet")
        } catch (e: retrofit2.HttpException) {
            throw Exception("Error del servidor: ${e.code()}")
        }
    }

    suspend fun actualizar(atraccion: Atraccion) {
        try {
            apiService.actualizarAtraccion(atraccion.id, atraccion)  // envía los cambios
            refrescarAtracciones()
        } catch (e: java.io.IOException) {
            throw Exception("Sin conexión a internet")
        } catch (e: retrofit2.HttpException) {
            throw Exception("Error del servidor: ${e.code()}")
        }
    }

    suspend fun eliminar(id: String) { // Cambiado a String
        try {
            apiService.eliminarAtraccion(id)  // le dice al servidor qué borrar
            refrescarAtracciones()
        } catch (e: java.io.IOException) {
            throw Exception("Sin conexión a internet")
        } catch (e: retrofit2.HttpException) {
            throw Exception("Error del servidor: ${e.code()}")
        }
    }

    suspend fun obtenerPorId(id: String): Atraccion? { // Cambiado a String
        return apiService.obtenerAtracciones().find { it.id == id }
    }
}
