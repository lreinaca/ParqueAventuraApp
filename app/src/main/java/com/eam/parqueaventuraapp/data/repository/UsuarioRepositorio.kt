package com.eam.parqueaventuraapp.data.modelo.repository

import com.eam.parqueaventuraapp.data.modelo.Usuario
import com.eam.parqueaventuraapp.data.modelo.database.UsuarioDao
import kotlinx.coroutines.flow.Flow

/**
 * CAPA 3: EL REPOSITORIO
 * 
 * Este es el puente entre la base de datos y la lógica de la aplicación.
 */
class UsuarioRepositorio(private val usuarioDao: UsuarioDao) {

    // Obtener todos los usuarios como un flujo de datos (Flow)
    val todosLosUsuarios: Flow<List<Usuario>> = usuarioDao.obtenerTodos()

    // Función para registrar un usuario llamando al DAO
    suspend fun insertar(usuario: Usuario) {
        usuarioDao.insertar(usuario)
    }

    // Función para validar login llamando al DAO
    suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo, clave)
    }
}
