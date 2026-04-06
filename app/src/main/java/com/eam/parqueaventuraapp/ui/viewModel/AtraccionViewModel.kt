package com.eam.parqueaventuraapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import com.eam.parqueaventuraapp.data.repository.AtraccionRepositorio
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AtraccionViewModel(private val repositorio: AtraccionRepositorio) : ViewModel() {

    // stateIn() convierte el Flow del repositorio a StateFlow
    val atracciones: StateFlow<List<Atraccion>> = repositorio.todasLasAtracciones
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Estado para operaciones de escritura (insertar, actualizar, eliminar)
    private val _operacionExitosa = MutableLiveData<Boolean?>()
    val operacionExitosa: LiveData<Boolean?> = _operacionExitosa

    fun insertar(atraccion: Atraccion) {
        viewModelScope.launch {
            repositorio.insertar(atraccion)
            _operacionExitosa.postValue(true)
        }
    }

    fun actualizar(atraccion: Atraccion) {
        viewModelScope.launch {
            repositorio.actualizar(atraccion)
            _operacionExitosa.postValue(true)
        }
    }

    // Reinicia el estado para no reaccionar al mismo evento dos veces
    fun resetOperacion() {
        _operacionExitosa.postValue(null)
    }

    // DATO DE EJEMPLO ====================================
    init {
        insertarDatosEjemplo()
    }
    private fun insertarDatosEjemplo(){
        viewModelScope.launch {

            repositorio.insertar(
                Atraccion(
                    id = 1,
                    nombre = "Carrusel Cascanuez",
                    tipo = "infantil",
                    duracion = 5,
                    tiempoEspera = 10,
                    estado = "ABIERTA",
                    imagen = "https://images.pexels.com/photos/13206487/pexels-photo-13206487.jpeg"
                )
            )
        }
    }

}

class AtraccionViewModelFactory(private val repositorio: AtraccionRepositorio) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AtraccionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AtraccionViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
