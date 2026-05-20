package com.eam.parqueaventuraapp.data.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Esta clase representa una ATRACCIÓN dentro del parque.
 *
 * @SerializedName asegura la compatibilidad con los campos de MockAPI.
 */
@Entity(tableName = "atracciones")
data class Atraccion(
    @PrimaryKey
    @SerializedName("id")
    val id: String = "0",

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("imagen")
    val imagen: String,

    @SerializedName("tiempoEspera")
    val tiempoEspera: Int,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("duracion")
    val duracion: Int
)
