package com.eam.parqueaventuraapp.ui.pantallas.admins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel

@Composable
fun PantallaGestionAtracciones(navController: NavController, atraccionViewModel: AtraccionViewModel) {
    LaunchedEffect(Unit) {
        atraccionViewModel.refrescar()
    }

    // CONSUMO DE ESTADOS YA FILTRADOS DESDE EL VIEWMODEL
    val uiState by atraccionViewModel.uiStateGestionAdmin.collectAsState()
    
    val atraccionesActivas = uiState.activas
    val atraccionesInactivas = uiState.inactivas

    var mostrarDialogo by remember { mutableStateOf(false) }
    var atraccionAInactivar by remember { mutableStateOf<Atraccion?>(null) }
    val listState = rememberLazyListState()

    if (mostrarDialogo && atraccionAInactivar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text(text = "Inactivar Atracción", fontWeight = FontWeight.Bold) },
            text = { Text(text = "¿Estás seguro de que deseas inactivar '${atraccionAInactivar?.nombre}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        atraccionAInactivar?.let {
                            atraccionViewModel.actualizar(it.copy(estado = "INACTIVA"))
                        }
                        mostrarDialogo = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Confirmar", color = Color.White) }
            },
            dismissButton = { TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = {
            BarraSuperiorAtracciones(
                totalAtracciones = atraccionesActivas.size,
                onVolver = { navController.popBackStack() },
                onCrear = { navController.navigate("crearAtraccion") }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(FondoPantalla)) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = padding.calculateTopPadding() + 12.dp, bottom = 18.dp)
            ) {
                item {
                    Text(text = "Atracciones en el Parque", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontWeight = FontWeight.Bold)
                }

                items(atraccionesActivas) { atraccion ->
                    ItemAtraccionAdmin(
                        atraccion = atraccion,
                        onEditar = { navController.navigate("editarAtraccion/${atraccion.id}") },
                        onInactivar = {
                            atraccionAInactivar = atraccion
                            mostrarDialogo = true
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (atraccionesInactivas.isNotEmpty()) {
                    item {
                        Text(text = "Archivo (Inactivas)", modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), fontWeight = FontWeight.Bold, color = TextoSecundario)
                    }
                    items(atraccionesInactivas) { atraccion ->
                        ItemAtraccionAdmin(
                            atraccion = atraccion,
                            onEditar = { navController.navigate("editarAtraccion/${atraccion.id}") },
                            onInactivar = null
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorAtracciones(totalAtracciones: Int, onVolver: () -> Unit, onCrear: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onVolver) { Icon(Icons.Default.ArrowBack, null) }
        },
        title = {
            Column {
                Text("Gestionar Atracciones", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("$totalAtracciones activas", fontSize = 13.sp, color = TextoSecundario)
            }
        },
        actions = {
            IconButton(onClick = onCrear, modifier = Modifier.padding(end = 8.dp).background(VerdeAccent, CircleShape).size(36.dp)) {
                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun ItemAtraccionAdmin(atraccion: Atraccion, onEditar: () -> Unit, onInactivar: (() -> Unit)?) {
    val esInactiva = atraccion.estado == "INACTIVA"
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = if (esInactiva) Color(0xFFF5F5F5) else Color.White)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = atraccion.imagen, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(60.dp).clip(RoundedCornerShape(10.dp)))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(atraccion.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = if (esInactiva) TextoSecundario else TextoPrincipal)
                Text("${atraccion.tipo} • ${atraccion.duracion} min", fontSize = 12.sp, color = TextoSecundario)
            }
            Row {
                IconButton(onClick = onEditar) { Icon(Icons.Default.Edit, null, tint = VerdeClaro, modifier = Modifier.size(20.dp)) }
                if (onInactivar != null) {
                    IconButton(onClick = onInactivar) { Icon(Icons.Default.Delete, null, tint = Color.Red, modifier = Modifier.size(20.dp)) }
                }
            }
        }
    }
}
