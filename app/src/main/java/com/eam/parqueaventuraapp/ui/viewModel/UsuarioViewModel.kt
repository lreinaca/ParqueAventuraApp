package com.eam.parqueaventuraapp.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eam.parqueaventuraapp.data.modelo.Roles
import com.eam.parqueaventuraapp.data.modelo.Usuario
import com.eam.parqueaventuraapp.data.modelo.repository.UsuarioRepositorio
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repositorio: UsuarioRepositorio) : ViewModel() {

    val usuarios: StateFlow<List<Usuario>> = repositorio.todosLosUsuarios
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _loginStatus = MutableLiveData<Boolean?>()
    val loginStatus: LiveData<Boolean?> = _loginStatus

    private val _navegacionDestino = MutableSharedFlow<String>()
    val navegacionDestino = _navegacionDestino.asSharedFlow()

    private val _errorMensaje = MutableStateFlow<String?>(null)
    val errorMensaje = _errorMensaje.asStateFlow()

    // Estado para las credenciales recordadas
    private val _credencialesRecordadas = MutableStateFlow<Pair<String, String>>(Pair("", ""))
    val credencialesRecordadas: StateFlow<Pair<String, String>> = _credencialesRecordadas.asStateFlow()

    fun cargarCredenciales(context: Context) {
        val preferences = context.getSharedPreferences("credenciales", Context.MODE_PRIVATE)
        val user = preferences.getString("user", "") ?: ""
        val pass = preferences.getString("pass", "") ?: ""
        _credencialesRecordadas.value = Pair(user, pass)
    }

    private fun guardarCredenciales(context: Context, usuario: String, pass: String) {
        val preferences = context.getSharedPreferences("credenciales", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("user", usuario)
        editor.putString("pass", pass)
        editor.apply()
    }

    fun inicioSesion(context: Context, correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _errorMensaje.value = "Por favor, completa todos los campos"
            return
        }
        
        viewModelScope.launch {
            val usuario = repositorio.login(correo, clave)
            if (usuario != null) {
                guardarCredenciales(context, correo, clave)
                _usuarioActual.value = usuario
                _loginStatus.postValue(true)
                
                // Decidimos la navegación en el ViewModel
                val destino = if (usuario.rol == Roles.ADMIN) "panelAdmin" else "inicioUsuario"
                _navegacionDestino.emit(destino)
            } else {
                _loginStatus.postValue(false)
                _errorMensaje.value = "Correo o contraseña incorrectos"
            }
        }
    }

    fun registro(nombre: String, correo: String, clave: String) {
        if (nombre.isBlank() || correo.isBlank() || clave.isBlank()) {
            _errorMensaje.value = "Todos los campos son obligatorios"
            return
        }

        viewModelScope.launch {
            val usuarioExistente = repositorio.buscarPorCorreo(correo)
            if(usuarioExistente != null) {
                _loginStatus.postValue(false)
                _errorMensaje.value = "El correo ya está registrado"
            } else {
                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    correo = correo,
                    clave  = clave,
                    rol    = Roles.USUARIO
                )
                repositorio.insertar(nuevoUsuario)
                _loginStatus.postValue(true)
            }
        }
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
        _loginStatus.postValue(null)
        _errorMensaje.value = null
    }

    fun clearError() {
        _errorMensaje.value = null
    }

    init {
        crearAdmin()
    }

    private fun crearAdmin(){
        viewModelScope.launch {
            val adminExistente = repositorio.buscarPorCorreo("admin@parque.com")
            if (adminExistente == null) {
                repositorio.insertar(
                    Usuario(
                        nombre = "Admin sistema",
                        correo  = "admin@parque.com",
                        clave = "admin123",
                        rol = Roles.ADMIN
                    )
                )
            }
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
