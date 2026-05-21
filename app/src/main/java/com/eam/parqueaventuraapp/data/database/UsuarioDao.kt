package com.eam.parqueaventuraapp.data.modelo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eam.parqueaventuraapp.data.modelo.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave") // ":correo" por ejemplo, es un parametro
    suspend fun login(correo: String, clave: String): Usuario?

    // Nueva función para obtener todos los usuarios y que la lista se actualice sola (Flow)
    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuarios WHERE id = :id ")
    suspend fun buscarPorId(id: Int): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?
}
