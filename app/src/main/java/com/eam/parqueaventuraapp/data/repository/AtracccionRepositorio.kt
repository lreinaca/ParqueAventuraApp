package com.eam.parqueaventuraapp.data.repository

import com.eam.parqueaventuraapp.data.database.AtraccionDao
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import kotlinx.coroutines.flow.Flow

/**
 * REPOSITORIO:
 * Esta clase actúa como intermediario entre:
 *
 * ViewModel y la base de datos (DAO)
 */
class AtraccionRepositorio (private val atraccionDao: AtraccionDao){

    // Flujo que devuelve todas las atracciones
    val todasLasAtracciones: Flow<List<Atraccion>> = atraccionDao.obtenerTodas()

    // Inserta una nueva atracción en la base de datos
    suspend fun insertar(atraccion: Atraccion) {
        atraccionDao.insertar(atraccion)
    }

    // Actualiza una atracción ya existente
    suspend fun actualizar(atraccion: Atraccion) {
        atraccionDao.actualizar(atraccion)
    }

    // Obtiene una atracción por su ID
    suspend fun obtenerPorId(id: Int): Atraccion? {
        return atraccionDao.obtenerPorId(id)
    }
}