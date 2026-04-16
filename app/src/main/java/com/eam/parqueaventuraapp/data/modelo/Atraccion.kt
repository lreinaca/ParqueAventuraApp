package com.eam.parqueaventuraapp.data.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Esta clase representa una ATRACCIÓN dentro del parque.
 *
 * En Room, una clase con la anotación @Entity se convierte en una TABLA
 * dentro de la base de datos.
 */
@Entity(tableName = "atracciones")
data class Atraccion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    // Se guarda como String para simplificar la base de datos.
    val tipo: String,

    val imagen: String,

    // Tiempo en minutos
    val tiempoEspera: Int,

    /* Posibles valores:
    * - "ABIERTA"
    * - "CERRADA"
    * - "MANTENIMIENTO"
    * - "INACTIVA" */
    val estado: String,

    // Duración en minutos
    val duracion: Int
)