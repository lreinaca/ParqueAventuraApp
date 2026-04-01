package com.eam.parqueaventuraapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import com.eam.parqueaventuraapp.data.repository.AtraccionRepositorio
import kotlinx.coroutines.launch

class AtraccionViewModel(private val repositorio: AtraccionRepositorio) : ViewModel() {

    // asLiveData() convierte el Flow del repositorio a LiveData.
    // Así puedes observarlo con observeAsState() en Compose,
    // igual que haces con loginStatus en PantallaLogin.
    val atracciones: LiveData<List<Atraccion>> = repositorio.todasLasAtracciones.asLiveData()

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

    // DATOS DE EJEMPLO ====================================
    init {
        insertarDatosEjemplo()
    }
    private fun insertarDatosEjemplo(){
        viewModelScope.launch {

            repositorio.insertar(
                Atraccion(
                    id = 1,
                    nombre = "Montaña Rusa Jaguar",
                    tipo = "extrema",
                    duracion = 3,
                    tiempoEspera = 25,
                    estado = "ABIERTA",
                    imagen = "https://picsum.photos/300/200"
                )
            )

            repositorio.insertar(
                Atraccion(
                    id = 2,
                    nombre = "Carrusel Mágico",
                    tipo = "infantil",
                    duracion = 4,
                    tiempoEspera = 5,
                    estado = "ABIERTA",
                    imagen = "https://picsum.photos/300/201"
                )
            )
            repositorio.insertar(
                Atraccion(
                    id = 12,
                    nombre = "Carrusel terror",
                    tipo = "infantil",
                    duracion = 4,
                    tiempoEspera = 5,
                    estado = "ABIERTA",
                    imagen = "https://picsum.photos/300/201"
                )
            )
            repositorio.insertar(
                Atraccion(
                    id = 21,
                    nombre = "Carrusel arcoiris",
                    tipo = "infantil",
                    duracion = 4,
                    tiempoEspera = 5,
                    estado = "ABIERTA",
                    imagen = "https://picsum.photos/300/201"
                )
            )
            repositorio.insertar(
                Atraccion(
                    id = 20,
                    nombre = "montaña rusa",
                    tipo = "familiar",
                    duracion = 4,
                    tiempoEspera = 5,
                    estado = "ABIERTA",
                    imagen = "https://picsum.photos/300/201"
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
