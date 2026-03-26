package com.eam.parqueaventuraapp.data.modelo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eam.parqueaventuraapp.data.database.AtraccionDao
import com.eam.parqueaventuraapp.data.modelo.Usuario
import com.eam.parqueaventuraapp.data.modelo.Atraccion

// Definimos la configuración de la base de datos: 
// 1. 'entities' indica qué tablas contiene (en este caso, Usuario).
// 2. 'version' se debe aumentar si cambias la estructura de las tablas en el futuro.
@Database(
    entities = [
        Usuario::class,
        Atraccion::class
    ],
    version = 1
)
abstract class BaseDeDatosApp : RoomDatabase() {
    
    // Método abstracto que Room implementará para darnos acceso a las consultas (DAO)
    abstract fun usuarioDao(): UsuarioDao
    abstract fun atraccionDao(): AtraccionDao

    companion object {
        // '@Volatile' asegura que el valor de la instancia siempre esté actualizado para todos los hilos de ejecución
        @Volatile
        private var INSTANCIA: BaseDeDatosApp? = null

        // Función para obtener la base de datos. Implementa el patrón Singleton para evitar abrir múltiples 
        // conexiones al mismo archivo, lo cual sería costoso para el rendimiento.
        fun obtenerBaseDeDatos(contexto: Context): BaseDeDatosApp {
            // Si 'INSTANCIA' no es nula, la devolvemos. Si es nula, entramos al bloque sincronizado para crearla.
            return INSTANCIA ?: synchronized(this) {
                // Construimos la base de datos usando el contexto de la aplicación
                val instancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDeDatosApp::class.java,
                    "parque_aventura_db" // Nombre del archivo de la base de datos en el teléfono
                ).build()
                
                // Guardamos la instancia creada para reutilizarla después
                INSTANCIA = instancia
                
                // Retornamos la nueva instancia
                instancia
            }
        }
    }
}
