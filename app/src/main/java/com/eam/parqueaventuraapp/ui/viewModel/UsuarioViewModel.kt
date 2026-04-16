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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
        viewModelScope.launch {
            val usuario = repositorio.login(correo, clave)
            if (usuario != null) {
                guardarCredenciales(context, correo, clave)
            }
            _usuarioActual.value = usuario
            _loginStatus.postValue(usuario != null)
        }
    }

    fun registro(nombre: String, correo: String, clave: String) {
        viewModelScope.launch {
            val usuarioExistente = repositorio.buscarPorCorreo(correo)
            if(usuarioExistente != null) {
                _loginStatus.postValue(false)
            } else {
                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    correo = correo,
                    clave  = clave,
                    rol    = Roles.USUARIO
                )
                repositorio.insertar(nuevoUsuario)
            }
        }
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
        _loginStatus.postValue(null)
    }

    init {
        crearAdmin()
    }

    private fun crearAdmin(){
        viewModelScope.launch {
            repositorio.insertar(
                Usuario(
                    id = 1, // ID fijo para el admin inicial
                    nombre = "Admin sistema",
                    correo  = "admin@parque.com",
                    clave = "admin123",
                    rol = Roles.ADMIN
                )
            )
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
