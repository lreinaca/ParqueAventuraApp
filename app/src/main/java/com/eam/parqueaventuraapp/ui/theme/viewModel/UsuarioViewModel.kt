package com.eam.parqueaventuraapp.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eam.parqueaventuraapp.data.model.Usuario
import com.eam.parqueaventuraapp.data.model.repository.UsuarioRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repositorio: UsuarioRepositorio) : ViewModel() {

    // Exponemos la lista de usuarios para que la UI pueda observarla
    val usuarios: Flow<List<Usuario>> = repositorio.todosLosUsuarios

    fun registro(nombre: String, correo: String, clave: String) {
        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                nombre = nombre,
                correo = correo,
                clave = clave
            )
            repositorio.insertar(nuevoUsuario)
        }
    }
}

class UsuarioViewModelFactory(private val repositorio: UsuarioRepositorio) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuarioViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
