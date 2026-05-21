package com.eam.parqueaventuraapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import kotlinx.coroutines.flow.Flow


/**
 * DAO (Data Access Object) : Es la capa que define TODAS
 * las operaciones que se pueden hacer sobre la tabla "atracciones".
 */
@Dao
interface AtraccionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(atraccion: Atraccion)

    @Query("SELECT * FROM atracciones")
    fun obtenerTodas(): Flow<List<Atraccion>>

    @Query("SELECT * FROM atracciones WHERE id = :id")
    suspend fun obtenerPorId(id: String): Atraccion?

    @Update
    suspend fun actualizar(atraccion: Atraccion)
}
