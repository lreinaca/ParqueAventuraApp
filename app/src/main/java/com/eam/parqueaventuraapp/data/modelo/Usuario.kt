package com.eam.parqueaventuraapp.data.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * CAPA 1: EL MODELO DE DATOS
 * 
 * Aquí definimos qué información vamos a guardar de un Usuario.
 * La anotación @Entity convierte esta clase en una tabla de la base de datos.
 */
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String
)
