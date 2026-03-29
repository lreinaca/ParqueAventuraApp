package com.eam.parqueaventuraapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eam.parqueaventuraapp.data.modelo.Roles
import com.eam.parqueaventuraapp.data.modelo.Usuario
import com.eam.parqueaventuraapp.data.modelo.repository.UsuarioRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repositorio: UsuarioRepositorio) : ViewModel() {

    // Exponemos la lista de usuarios para que la UI pueda observarla
    val usuarios: Flow<List<Usuario>> = repositorio.todosLosUsuarios

    private val _usuarioActual = MutableLiveData<Usuario?>()
    val usuarioActual : LiveData<Usuario?> = _usuarioActual

    private val _loginStatus = MutableLiveData<Boolean?>()
    val loginStatus: LiveData<Boolean?> = _loginStatus

    fun registro(nombre: String, correo: String, clave: String) {
        viewModelScope.launch {
            val usuarioExistente = repositorio.buscarPorCorreo(correo)

            if(usuarioExistente != null) { //si el usuario ya existe
                _loginStatus.postValue(false) // No le permite porque ya hay un usuario con ese correo

            }else{

                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    correo = correo,
                    clave = clave,
                    rol = Roles.USUARIO // Por defecto, el rol es "usuario"
                )
                repositorio.insertar(nuevoUsuario)

            }

        }
    }

    fun inicioSesion(correo: String, clave: String){
        viewModelScope.launch {
            val usuario = repositorio.login(correo,clave)
            _usuarioActual.postValue(usuario)
            _loginStatus.postValue(usuario != null)
        }
    }

    fun cerrarSesion() {
        _usuarioActual.postValue(null)
        _loginStatus.postValue(false)
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
