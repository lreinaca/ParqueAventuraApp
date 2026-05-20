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
        val lista = apiService.obtenerAtracciones()
        _atracciones.value = lista
    }

    suspend fun insertar(atraccion: Atraccion) {
        apiService.crearAtraccion(atraccion)
        refrescarAtracciones()
    }

    suspend fun actualizar(atraccion: Atraccion) {
        apiService.actualizarAtraccion(atraccion.id, atraccion)
        refrescarAtracciones()
    }

    suspend fun eliminar(id: String) { // Cambiado a String
        apiService.eliminarAtraccion(id)
        refrescarAtracciones()
    }

    suspend fun obtenerPorId(id: String): Atraccion? { // Cambiado a String
        return apiService.obtenerAtracciones().find { it.id == id }
    }
}
