package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.eam.parqueaventuraapp.R
import com.eam.parqueaventuraapp.ui.componentes.BarraNavegacionInferior
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PantallaMisFavoritos(navController: NavController, viewModel: UsuarioViewModel, atraccionViewModel: AtraccionViewModel) {
    // Reuse filtering logic from AtraccionViewModel, then restrict to favorites
    val favoritosIds by atraccionViewModel.favoritos.collectAsState()
    val atraccionesFiltradas by atraccionViewModel.atraccionesFiltradas.collectAsState()
    val textoBusqueda by atraccionViewModel.textoBusqueda.collectAsState()
    val categoriaSeleccionada by atraccionViewModel.categoriaSeleccionada.collectAsState()
    val estadoFiltro by atraccionViewModel.estadoFiltro.collectAsState()
    val tiempoMaxEspera by atraccionViewModel.tiempoMaxEspera.collectAsState()
    val cargando by atraccionViewModel.cargando.collectAsState()

    val favoritos = atraccionesFiltradas.filter { favoritosIds.contains(it.id) }

    var mostrarFiltros by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        bottomBar = {
            BarraNavegacionInferior(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Mis Favoritos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(text = "${favoritos.size} atracciones guardadas", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            // --- Buscador ---
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { atraccionViewModel.actualizarBusqueda(it) },
                placeholder = { Text("Buscar en favoritos...", color = Color.Gray) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar") },
                trailingIcon = {
                    IconButton(onClick = { mostrarFiltros = true }) {
                        Icon(imageVector = Icons.Default.Tune, contentDescription = "Filtros")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- Chips de categoría ---
            val opcionesFiltro = listOf("Todas", "Familiar", "Extrema", "Infantil")
            LazyRow(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 0.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(opcionesFiltro) { opcion ->
                    ChipFiltroCatalogo(
                        texto = opcion,
                        seleccionado = categoriaSeleccionada == opcion,
                        onClick = { atraccionViewModel.actualizarCategoria(opcion) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Modal de filtros avanzados ---
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
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ajusta los filtros para encontrar la atracción ideal",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(color = Color.LightGray, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(20.dp))

                        // Estado
                        Text(text = "Estado de la atracción", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        val opcionesEstado = listOf("Todas", "ABIERTA", "CERRADA", "MANTENIMIENTO")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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

                        Text(text = "Tiempo máximo de espera: ${tiempoMaxEspera.toInt()} min", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Slider(
                            value = tiempoMaxEspera,
                            onValueChange = { atraccionViewModel.actualizarTiempoMax(it) },
                            valueRange = 5f..60f,
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(thumbColor = Color(0xFF4CAF50), activeTrackColor = Color(0xFF4CAF50))
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f).border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)).background(Color.White, RoundedCornerShape(12.dp))) {
                                TextButton(onClick = { atraccionViewModel.resetFiltros(); mostrarFiltros = false }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                                    Text(text = "Limpiar", color = Color.Black, fontWeight = FontWeight.SemiBold)
                                }
                            }
                            Box(modifier = Modifier.weight(1f).background(Color(0xFF4CAF50), RoundedCornerShape(12.dp))) {
                                TextButton(onClick = { mostrarFiltros = false }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                                    Text(text = "Aplicar", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            if (favoritos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No hay atracciones en favoritos todavía.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(favoritos, key = { it.id }) { atraccion ->
                        TarjetaAtraccionFavorita(
                            atraccion = atraccion,
                            onClick = { navController.navigate("detalle/${atraccion.id}") },
                            onToggleFavorito = { atraccionViewModel.toggleFavorito(atraccion.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaAtraccionFavorita(
    atraccion: com.eam.parqueaventuraapp.data.modelo.Atraccion,
    onClick: () -> Unit = {},
    onToggleFavorito: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 115.dp), // Altura mínima flexible
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
            ) {
                AsyncImage(
                    model = atraccion.imagen.ifBlank { R.drawable.ic_launcher_background },
                    contentDescription = atraccion.nombre,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_launcher_background),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background)
                )
                Surface(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.TopStart),
                    color = Color(0xFFFF9800),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = atraccion.tipo.replaceFirstChar { it.uppercase() },
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = atraccion.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    IconButton(onClick = onToggleFavorito) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Eliminar favorito",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                Text(
                    text = atraccion.tipo.replaceFirstChar { it.uppercase() } + " • ${atraccion.duracion} min",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${atraccion.tiempoEspera} min",
                                color = Color(0xFF2E7D32),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Text(
                        text = atraccion.estado.replaceFirstChar { it.uppercase() },
                        color = Color(0xFF2E7D32),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
