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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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


// PANTALLA CATÁLOGO DE ATRACCIONES (USUARIO)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogoAtracciones(
    navController: NavController,
    atraccionViewModel: AtraccionViewModel
) {
    // observamos las atracciones desde el viewmodel (se actualiza solo si el admin agrega nuevas)
    val atracciones by atraccionViewModel.atracciones.collectAsState()

    // estado del texto de busqueda
    var textoBusqueda by remember { mutableStateOf("") }

    // filtro seleccionado, por defecto mostramos todas
    var filtroSeleccionado by remember { mutableStateOf("Todas") }

    // lista de opciones para los chips de filtro
    val opcionesFiltro = listOf("Todas", "Familiar", "Extrema", "Infantil")
    // controla si el modal de filtros está visible o no
    var mostrarFiltros by remember { mutableStateOf(false) }

    // filtros avanzados dentro del modal
    var filtroEstado by remember { mutableStateOf("Todas") }
    var filtroTiempoMax by remember { mutableStateOf(60f) } // maximo 60 min

    // estado del BottomSheet (animación de apertura/cierre)
    val sheetState = rememberModalBottomSheetState()


    // aca aplicamos los filtros sobre la lista original
    // primero filtramos por categoria y luego por el texto de busqueda
    val atraccionesFiltradas = atracciones.filter { atraccion ->

        // si es "Todas" no filtra por tipo, si no compara con el tipo en minusculas
        val cumpleFiltro = if (filtroSeleccionado == "Todas") {
            true
        } else {
            atraccion.tipo.lowercase() == filtroSeleccionado.lowercase()
        }

        // verificamos si el nombre contiene el texto que escribió el usuario
        val cumpleBusqueda = if (textoBusqueda.isBlank()) {
            true
        } else {
            atraccion.nombre.lowercase().contains(textoBusqueda.lowercase())
        }

        // filtro por estado (viene del modal)
        val cumpleEstado = if (filtroEstado == "Todas") {
            true
        } else {
            atraccion.estado.uppercase() == filtroEstado.uppercase()
        }

        // filtro por tiempo maximo de espera (viene del modal)
        val cumpleTiempo = atraccion.tiempoEspera <= filtroTiempoMax.toInt()

        // solo pasan las que cumplen TODOS los filtros
        cumpleFiltro && cumpleBusqueda && cumpleEstado && cumpleTiempo
    }

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
                // titulo del modal
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

                // chips de estado
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val opcionesEstado = listOf("Todas", "ABIERTA", "CERRADA", "MANTENIMIENTO")
                    items(opcionesEstado) { opcion ->
                        // texto legible para cada opcion
                        val textoVisible = when (opcion) {
                            "ABIERTA" -> "Activa"
                            "CERRADA" -> "Cerrada"
                            "MANTENIMIENTO" -> "Mantenimiento"
                            else -> "Todas"
                        }
                        ChipFiltroCatalogo(
                            texto = textoVisible,
                            seleccionado = filtroEstado == opcion,
                            onClick = { filtroEstado = opcion }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- FILTRO POR TIEMPO DE ESPERA ---
                Text(
                    text = "Tiempo máximo de espera: ${filtroTiempoMax.toInt()} min",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextoPrincipal
                )

                Spacer(modifier = Modifier.height(10.dp))

                // slider para elegir el tiempo maximo
                Slider(
                    value = filtroTiempoMax,
                    onValueChange = { filtroTiempoMax = it },
                    valueRange = 5f..60f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = VerdeAccent,
                        activeTrackColor = VerdeAccent
                    )
                )

                // etiquetas del slider
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

                // --- BOTONES DEL MODAL ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // boton para limpiar todos los filtros
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, BordeInactivo, RoundedCornerShape(12.dp))
                            .background(Color.White, RoundedCornerShape(12.dp))
                    ) {
                        TextButton(
                            onClick = {
                                // reseteamos los filtros a sus valores por defecto
                                filtroEstado = "Todas"
                                filtroTiempoMax = 60f
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Limpiar",
                                color = TextoPrincipal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // boton para aplicar y cerrar el modal
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(VerdeAccent, RoundedCornerShape(12.dp))
                    ) {
                        TextButton(
                            onClick = { mostrarFiltros = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Aplicar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // espacio extra para que no quede pegado al borde inferior
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }


    Scaffold(
        bottomBar = {
            // reutilizamos la barra de navegación que ya existe en componentes
            BarraNavegacionInferior(navController)
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantalla)
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
        ) {


            // ENCABEZADO: TÍTULO DE LA PANTALLA

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


            // BARRA DE BÚSQUEDA

            item {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    placeholder = {
                        Text("Buscar atracción...", color = TextoSecundario)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = TextoSecundario
                        )
                    },
                    trailingIcon = {
                        // icono de filtro a la derecha como en la imagen
                        IconButton(onClick = { mostrarFiltros = true }) {
                        Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Filtros",
                                tint = TextoSecundario
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
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


            // FILA DE CHIPS DE FILTRO (Todas, Familiar, Extrema, Infantil)

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(opcionesFiltro) { opcion ->
                        ChipFiltroCatalogo(
                            texto = opcion,
                            seleccionado = filtroSeleccionado == opcion,
                            onClick = { filtroSeleccionado = opcion }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }


            // CONTADOR DE RESULTADOS

            item {
                Text(
                    text = "${atraccionesFiltradas.size} atracciones encontradas",
                    fontSize = 13.sp,
                    color = TextoSecundario,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }


            // LISTA DE ATRACCIONES (reutilizamos ItemAtraccion del Dashboard)

            if (atraccionesFiltradas.isEmpty()) {
                // si no hay resultados mostramos un mensaje
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron atracciones",
                            color = TextoSecundario,
                            fontSize = 15.sp
                        )
                    }
                }
            } else {
                // recorremos la lista filtrada y mostramos cada tarjeta
                items(atraccionesFiltradas) { atraccion ->
                    // reutilizamos ItemAtraccion que ya está en DashboardUsuarioPantalla
                    // al hacer clic navega al detalle de esa atraccion
                    ItemAtraccion(
                        atraccion = atraccion,
                        onVerDetalle = { navController.navigate("detalle/${atraccion.id}") }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}



// CHIP DE FILTRO PARA EL CATÁLOGO

@Composable
fun ChipFiltroCatalogo(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    // si está seleccionado el fondo es verde, si no es blanco con borde
    val fondo = if (seleccionado) VerdeAccent else Color.White
    val colorTexto = if (seleccionado) Color.White else TextoPrincipal

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (seleccionado) VerdeAccent else BordeInactivo,
                shape = RoundedCornerShape(20.dp)
            )
            .background(fondo, RoundedCornerShape(20.dp))
    ) {
        TextButton(onClick = onClick) {
            Text(
                text = texto,
                color = colorTexto,
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}


