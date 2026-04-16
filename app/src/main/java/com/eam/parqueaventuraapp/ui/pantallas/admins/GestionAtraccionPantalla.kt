package com.eam.parqueaventuraapp.ui.pantallas.admins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    val todasLasAtracciones by atraccionViewModel.atracciones.collectAsState()
    
    // Para el admin mostramos todas, pero podemos resaltar las INACTIVAS
    val atraccionesActivas = todasLasAtracciones.filter { it.estado != "INACTIVA" }
    val atraccionesInactivas = todasLasAtracciones.filter { it.estado == "INACTIVA" }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var atraccionAInactivar by remember { mutableStateOf<Atraccion?>(null) }

    if (mostrarDialogo && atraccionAInactivar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text(text = "Inactivar Atracción", fontWeight = FontWeight.Bold) },
            text = { Text(text = "¿Estás seguro de que deseas inactivar '${atraccionAInactivar?.nombre}'? Ya no será visible para los usuarios.") },
            confirmButton = {
                Button(
                    onClick = {
                        atraccionAInactivar?.let {
                            atraccionViewModel.actualizar(it.copy(estado = "INACTIVA"))
                        }
                        mostrarDialogo = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Confirmar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
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
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(FondoPantalla),
            contentPadding = PaddingValues(top = padding.calculateTopPadding() + 12.dp, bottom = 12.dp)
        ) {
            item {
                Text(
                    text = "Atracciones en el Parque",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
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
                    Text(
                        text = "Archivo (Inactivas)",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        fontWeight = FontWeight.Bold,
                        color = TextoSecundario
                    )
                }
                items(atraccionesInactivas) { atraccion ->
                    ItemAtraccionAdmin(
                        atraccion = atraccion,
                        onEditar = { navController.navigate("editarAtraccion/${atraccion.id}") },
                        onInactivar = null // No inactivar lo que ya está inactivo
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorAtracciones(totalAtracciones: Int, onVolver: () -> Unit, onCrear: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onVolver) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null) }
        },
        title = {
            Column {
                Text(text = "Gestionar Atracciones", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "$totalAtracciones activas", fontSize = 13.sp, color = TextoSecundario)
            }
        },
        actions = {
            IconButton(
                onClick = onCrear,
                modifier = Modifier.padding(end = 8.dp).background(VerdeAccent, CircleShape).size(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun ItemAtraccionAdmin(atraccion: Atraccion, onEditar: () -> Unit, onInactivar: (() -> Unit)?) {
    val esInactiva = atraccion.estado == "INACTIVA"
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = if (esInactiva) Color(0xFFF5F5F5) else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (esInactiva) 0.dp else 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = atraccion.imagen,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(10.dp)).then(if (esInactiva) Modifier.background(Color.Gray) else Modifier)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = atraccion.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = if (esInactiva) TextoSecundario else TextoPrincipal)
                Text(text = "${atraccion.tipo} • ${atraccion.duracion} min", fontSize = 12.sp, color = TextoSecundario)
            }
            
            Row {
                IconButton(onClick = onEditar) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = VerdeClaro, modifier = Modifier.size(20.dp))
                }
                if (onInactivar != null) {
                    IconButton(onClick = onInactivar) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}
