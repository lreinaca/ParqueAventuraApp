package com.eam.parqueaventuraapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eam.parqueaventuraapp.data.modelo.Roles
import com.eam.parqueaventuraapp.data.modelo.Usuario
import com.eam.parqueaventuraapp.data.modelo.repository.UsuarioRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repositorio: UsuarioRepositorio) : ViewModel() {

    // Exponemos la lista de usuarios para que la UI pueda observarla
    val usuarios: StateFlow<List<Usuario>> = repositorio.todosLosUsuarios
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // MutableStateFlow: siempre tiene un valor (null al inicio, un Usuario después del login)
    // asStateFlow() lo hace de solo lectura para quien lo observe desde la UI
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    // loginStatus sigue siendo LiveData porque PantallaLogin lo usa
    // con observeAsState(), y ese archivo no lo estamos migrando.
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
                    clave  = clave,
                    rol    = Roles.USUARIO // Por defecto, el rol es "usuario"
                )
                repositorio.insertar(nuevoUsuario)

            }
        }
    }

    fun inicioSesion(correo: String, clave: String){
        viewModelScope.launch {
            val usuario = repositorio.login(correo, clave)

            // Con StateFlow se asigna el valor con .value,
            // sin necesidad de .postValue() como en LiveData.
            _usuarioActual.value = usuario
            _loginStatus.postValue(usuario != null)
        }
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
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