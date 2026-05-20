package com.eam.parqueaventuraapp.ui.viewModel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import com.eam.parqueaventuraapp.data.repository.AtraccionRepositorio
import com.eam.parqueaventuraapp.ui.theme.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// MODELOS DE ESTADO PARA LA UI (Cero lógica en la pantalla)
data class AdminStats(
    val total: Int = 0,
    val activas: Int = 0,
    val mantenimiento: Int = 0,
    val cerradas: Int = 0,
    val recientes: List<Atraccion> = emptyList()
)

data class GestionAdminState(
    val activas: List<Atraccion> = emptyList(),
    val inactivas: List<Atraccion> = emptyList()
)

data class DashboardUsuarioState(
    val totalAtracciones: Int = 0,
    val disponibles: Int = 0,
    val listaAtracciones: List<Atraccion> = emptyList()
)

data class AtraccionDetalleUI(
    val nivelIntensidad: String = "",
    val valorProgreso: Float = 0f,
    val color: Color = Color.Gray,
    val zonaParque: String = ""
)

class AtraccionViewModel(private val repositorio: AtraccionRepositorio) : ViewModel() {

    // --- ESTADOS DE FILTROS ---
    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda = _textoBusqueda.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow("Todas")
    val categoriaSeleccionada = _categoriaSeleccionada.asStateFlow()

    private val _estadoFiltro = MutableStateFlow("Todas")
    val estadoFiltro = _estadoFiltro.asStateFlow()

    private val _tiempoMaxEspera = MutableStateFlow(60f)
    val tiempoMaxEspera = _tiempoMaxEspera.asStateFlow()

    // --- FLUJOS DE DATOS PROCESADOS ---

    // 1. Catálogo filtrado
    val atraccionesFiltradas: StateFlow<List<Atraccion>> = combine(
        repositorio.todasLasAtracciones, _textoBusqueda, _categoriaSeleccionada, _estadoFiltro, _tiempoMaxEspera
    ) { lista, busqueda, cat, estado, tiempo ->
        lista.filter {
            (cat == "Todas" || it.tipo.lowercase() == cat.lowercase()) &&
            (busqueda.isBlank() || it.nombre.lowercase().contains(busqueda.lowercase())) &&
            (estado == "Todas" || it.estado.uppercase() == estado.uppercase()) &&
            (it.tiempoEspera <= tiempo.toInt())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 2. Dashboard Usuario
    val uiStateUsuario: StateFlow<DashboardUsuarioState> = repositorio.todasLasAtracciones.map { lista ->
        val visibles = lista.filter { it.estado != "INACTIVA" }
        DashboardUsuarioState(
            totalAtracciones = visibles.size,
            disponibles = visibles.count { it.estado == "ABIERTA" },
            listaAtracciones = visibles.filter { it.estado == "ABIERTA" }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUsuarioState())

    // 3. Estadísticas Admin
    val estadisticasAdmin: StateFlow<AdminStats> = repositorio.todasLasAtracciones.map { lista ->
        AdminStats(
            total = lista.size,
            activas = lista.count { it.estado == "ABIERTA" },
            mantenimiento = lista.count { it.estado == "MANTENIMIENTO" },
            cerradas = lista.count { it.estado == "CERRADA" },
            recientes = lista.sortedByDescending { it.id }.take(3)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminStats())

    // 4. Gestión Admin
    val uiStateGestionAdmin: StateFlow<GestionAdminState> = repositorio.todasLasAtracciones.map { lista ->
        GestionAdminState(
            activas = lista.filter { it.estado != "INACTIVA" },
            inactivas = lista.filter { it.estado == "INACTIVA" }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GestionAdminState())

    // --- LÓGICA DE APOYO ---

    fun obtenerDetalleUI(atraccion: Atraccion): AtraccionDetalleUI {
        return when (atraccion.tipo.lowercase()) {
            "infantil" -> AtraccionDetalleUI("Muy Baja", 0.2f, ColorInfantil, "Zona Infantil")
            "familiar" -> AtraccionDetalleUI("Media", 0.5f, ColorFamiliar, "Zona Familiar")
            "extrema" -> AtraccionDetalleUI("Alta", 0.85f, ColorExtrema, "Zona Extrema")
            else -> AtraccionDetalleUI("Normal", 0.5f, Color.Gray, "Zona General")
        }
    }

    fun buscarAtraccionPorNombre(query: String): Atraccion? {
        return (repositorio.todasLasAtracciones as? MutableStateFlow)?.value?.find { it.nombre.contains(query, ignoreCase = true) }
    }

    // --- OPERACIONES CRUD ---
    val atracciones: StateFlow<List<Atraccion>> = repositorio.todasLasAtracciones.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _operacionExitosa = MutableLiveData<Boolean?>()
    val operacionExitosa: LiveData<Boolean?> = _operacionExitosa

    init { refrescar() }
    fun refrescar() { viewModelScope.launch { repositorio.refrescarAtracciones() } }
    fun insertar(a: Atraccion) { viewModelScope.launch { try { repositorio.insertar(a); _operacionExitosa.postValue(true) } catch(e: Exception) { _operacionExitosa.postValue(false) } } }
    fun actualizar(a: Atraccion) { viewModelScope.launch { try { repositorio.actualizar(a); _operacionExitosa.postValue(true) } catch(e: Exception) { _operacionExitosa.postValue(false) } } }
    fun eliminar(id: String) { viewModelScope.launch { try { repositorio.eliminar(id); _operacionExitosa.postValue(true) } catch(e: Exception) { _operacionExitosa.postValue(false) } } }
    fun resetOperacion() { _operacionExitosa.postValue(null) }
    fun actualizarBusqueda(t: String) { _textoBusqueda.value = t }
    fun actualizarCategoria(c: String) { _categoriaSeleccionada.value = c }
    fun actualizarEstadoFiltro(e: String) { _estadoFiltro.value = e }
    fun actualizarTiempoMax(t: Float) { _tiempoMaxEspera.value = t }
    fun resetFiltros() { _estadoFiltro.value = "Todas"; _tiempoMaxEspera.value = 60f }
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
