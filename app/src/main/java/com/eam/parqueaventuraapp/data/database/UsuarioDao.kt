package com.eam.parqueaventuraapp.data.modelo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.eam.parqueaventuraapp.data.modelo.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave")
    suspend fun login(correo: String, clave: String): Usuario?

    // Nueva función para obtener todos los usuarios y que la lista se actualice sola (Flow)
    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): Flow<List<Usuario>>
}
