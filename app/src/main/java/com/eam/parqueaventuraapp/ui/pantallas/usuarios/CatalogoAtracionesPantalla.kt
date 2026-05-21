package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.ui.componentes.BarraNavegacionInferior
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogoAtracciones(
    navController: NavController,
    atraccionViewModel: AtraccionViewModel
) {
    LaunchedEffect(Unit) {
        atraccionViewModel.refrescar()
    }

    // 1. Obtenemos los estados directamente del ViewModel
    val atraccionesFiltradas by atraccionViewModel.atraccionesFiltradas.collectAsState()
    val textoBusqueda by atraccionViewModel.textoBusqueda.collectAsState()
    val categoriaSeleccionada by atraccionViewModel.categoriaSeleccionada.collectAsState()
    val estadoFiltro by atraccionViewModel.estadoFiltro.collectAsState()
    val tiempoMaxEspera by atraccionViewModel.tiempoMaxEspera.collectAsState()
    val cargando by atraccionViewModel.cargando.collectAsState()
    val mensajeError by atraccionViewModel.mensajeOperacion.observeAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarFiltros by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val opcionesFiltro = listOf("Todas", "Familiar", "Extrema", "Infantil")
    val listState = rememberLazyListState()

    // MODAL DE FILTROS AVANZADOS (BottomSheet)
    if (mostrarFiltros) {
        ModalBottomSheet(
            onDismissRequest = { mostrarFiltros = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Filtros avanzados",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextoPrincipal
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ajusta los filtros para encontrar la atracción ideal",
                    fontSize = 13.sp,
                    color = TextoSecundario
                )
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = BordeInactivo, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(20.dp))

                // --- FILTRO POR ESTADO ---
                Text(
                    text = "Estado de la atracción",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextoPrincipal
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    val opcionesEstado = listOf("Todas", "ABIERTA", "CERRADA", "MANTENIMIENTO")
                    items(opcionesEstado) { opcion ->
                        val textoVisible = when (opcion) {
                            "ABIERTA" -> "Activa"
                            "CERRADA" -> "Cerrada"
                            "MANTENIMIENTO" -> "Mantenimiento"
                            else -> "Todas"
                        }
                        ChipFiltroCatalogo(
                            texto = textoVisible,
                            seleccionado = estadoFiltro == opcion,
                            onClick = { atraccionViewModel.actualizarEstadoFiltro(opcion) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- FILTRO POR TIEMPO DE ESPERA ---
                Text(
                    text = "Tiempo máximo de espera: ${tiempoMaxEspera.toInt()} min",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextoPrincipal
                )
                Spacer(modifier = Modifier.height(10.dp))
                Slider(
                    value = tiempoMaxEspera,
                    onValueChange = { atraccionViewModel.actualizarTiempoMax(it) },
                    valueRange = 5f..60f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = VerdeAccent,
                        activeTrackColor = VerdeAccent
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("5 min", fontSize = 11.sp, color = TextoSecundario)
                    Text("30 min", fontSize = 11.sp, color = TextoSecundario)
                    Text("60 min", fontSize = 11.sp, color = TextoSecundario)
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = BordeInactivo, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, BordeInactivo, RoundedCornerShape(12.dp))
                            .background(Color.White, RoundedCornerShape(12.dp))
                    ) {
                        TextButton(
                            onClick = { atraccionViewModel.resetFiltros() },
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Text(text = "Limpiar", color = TextoPrincipal, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(VerdeAccent, RoundedCornerShape(12.dp))
                    ) {
                        TextButton(
                            onClick = { mostrarFiltros = false },
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Text(text = "Aplicar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { BarraNavegacionInferior(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantalla)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Atracciones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = TextoPrincipal,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    OutlinedTextField(
                        value = textoBusqueda,
                        onValueChange = { atraccionViewModel.actualizarBusqueda(it) },
                        placeholder = { Text("Buscar atracción...", color = TextoSecundario) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar", tint = TextoSecundario) },
                        trailingIcon = {
                            IconButton(onClick = { mostrarFiltros = true }) {
                                Icon(imageVector = Icons.Default.Tune, contentDescription = "Filtros", tint = TextoSecundario)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = VerdeAccent,
                            unfocusedBorderColor = BordeInactivo
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(opcionesFiltro) { opcion ->
                            ChipFiltroCatalogo(
                                texto = opcion,
                                seleccionado = categoriaSeleccionada == opcion,
                                onClick = { atraccionViewModel.actualizarCategoria(opcion) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    Text(
                        text = "${atraccionesFiltradas.size} atracciones encontradas",
                        fontSize = 13.sp,
                        color = TextoSecundario,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (cargando) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator() // el spinner
                        }
                    }
                } else if (atraccionesFiltradas.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text(text = "No se encontraron atracciones", color = TextoSecundario)
                        }
                    }
                } else {
                    items(atraccionesFiltradas) { atraccion ->
                        ItemAtraccion(
                            atraccion = atraccion,
                            onVerDetalle = { navController.navigate("detalle/${atraccion.id}") }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            if (listState.canScrollForward || listState.canScrollBackward) {
                val totalItems = listState.layoutInfo.totalItemsCount.coerceAtLeast(1)
                val visibleItems = listState.layoutInfo.visibleItemsInfo.size.coerceAtLeast(1)
                val maxFirstVisibleIndex = (totalItems - visibleItems).coerceAtLeast(1)
                val progress = (listState.firstVisibleItemIndex.toFloat() / maxFirstVisibleIndex.toFloat()).coerceIn(0f, 1f)

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .height(48.dp)
                        .offset(y = (progress * 140).dp)
                        .width(3.dp)
                        .background(VerdeAccent.copy(alpha = 0.5f), RoundedCornerShape(3.dp))
                )
            }
        }
    }
    LaunchedEffect(mensajeError) {
        if (mensajeError != null) {
            snackbarHostState.showSnackbar(mensajeError!!)
            atraccionViewModel.resetOperacion()
        }
    }
}

@Composable
fun ChipFiltroCatalogo(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    val fondo = if (seleccionado) VerdeAccent else Color.White
    val colorTexto = if (seleccionado) Color.White else TextoPrincipal
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = if (seleccionado) VerdeAccent else BordeInactivo, shape = RoundedCornerShape(20.dp))
            .background(fondo, RoundedCornerShape(20.dp))
    ) {
        TextButton(onClick = onClick) {
            Text(text = texto, color = colorTexto, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp)
        }
    }
}
