package com.eam.parqueaventuraapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import kotlinx.coroutines.flow.Flow


/**
 * DAO (Data Access Object) : Es la capa que define TODAS
 * las operaciones que se pueden hacer sobre la tabla "atracciones".
 *
 */
@Dao
interface AtraccionDao {

    /* suspend significa que esta operación debe ejecutarse
    * dentro de una corrutina porque acceder a la base de datos
    * puede tardar tiempo y no debe bloquear la interfaz.
    */
    @Insert
    suspend fun insertar(atraccion: Atraccion)

    /* Se usa Flow para que la lista se actualice automáticamente
    * si la base de datos cambia.
    */
    @Query("SELECT * FROM atracciones")
    fun obtenerTodas(): Flow<List<Atraccion>>

    @Query("SELECT * FROM atracciones WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Atraccion?

    @Update
    suspend fun actualizar(atraccion: Atraccion)
}