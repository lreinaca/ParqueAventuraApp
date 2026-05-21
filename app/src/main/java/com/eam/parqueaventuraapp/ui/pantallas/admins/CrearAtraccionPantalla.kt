package com.eam.parqueaventuraapp.ui.pantallas.admins

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import com.eam.parqueaventuraapp.ui.theme.FondoPantalla
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import com.eam.parqueaventuraapp.ui.theme.*

@Composable
fun PantallaCrearAtraccion(navController: NavController, atraccionViewModel: AtraccionViewModel) {
    var nombre by remember { mutableStateOf("") }
    var tipoSelected by remember { mutableStateOf("familiar") }
    var duracion by remember { mutableStateOf(5f) }
    var tiempoEspera by remember { mutableStateOf(15f) }
    var estadoSelected by remember { mutableStateOf("ABIERTA") }
    var imagen by remember { mutableStateOf("") }

    val operacionExito by atraccionViewModel.operacionExitosa.observeAsState()

    LaunchedEffect(operacionExito) {
        if (operacionExito == true) {
            atraccionViewModel.resetOperacion()
            navController.popBackStack()
        }
    }

    val puedeGuardar = nombre.isNotBlank()

    Scaffold(
        topBar = {
            BarraSuperiorCrear(onVolver = { navController.popBackStack() })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantalla)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SeccionFormulario(titulo = "Nombre de la atracción") {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Ej: Torre del Terror", color = TextoSecundario) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            SeccionFormulario(titulo = "Tipo de atracción") {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf("familiar", "extrema", "infantil").forEach { tipo ->
                        ChipSeleccion(
                            texto = tipo,
                            seleccionado = tipoSelected == tipo,
                            onClick = { tipoSelected = tipo }
                        )
                    }
                }
            }

            SeccionFormulario(titulo = "Duración del ciclo: ${duracion.toInt()} min") {
                Slider(
                    value = duracion,
                    onValueChange = { duracion = it },
                    valueRange = 1f..60f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = VerdeAccent,
                        activeTrackColor = VerdeAccent
                    )
                )
            }

            SeccionFormulario(titulo = "Tiempo de espera: ${tiempoEspera.toInt()} min") {
                Slider(
                    value = tiempoEspera,
                    onValueChange = { tiempoEspera = it },
                    valueRange = 0f..60f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = VerdeAccent,
                        activeTrackColor = VerdeAccent
                    )
                )
            }

            SeccionFormulario(titulo = "Estado") {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ChipEstado(
                        texto = "Abierta",
                        seleccionado = estadoSelected == "ABIERTA",
                        colorActivo  = Color(0xFF5CB460),
                        onClick = { estadoSelected = "ABIERTA" },
                    )
                    ChipEstado(
                        texto = "Mantenimiento",
                        seleccionado = estadoSelected == "MANTENIMIENTO",
                        colorActivo = Color(0xFFFA9E16),
                        onClick = { estadoSelected = "MANTENIMIENTO" }
                    )
                    ChipEstado(
                        texto = "Cerrada",
                        seleccionado = estadoSelected == "CERRADA",
                        colorActivo = Color(0xFFCD3735),
                        onClick = { estadoSelected = "CERRADA" }
                    )
                }
            }

            SeccionFormulario(titulo = "URL de la imagen") {
                OutlinedTextField(
                    value = imagen,
                    onValueChange = { imagen = it },
                    placeholder = { Text("https://...", color = TextoSecundario) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Button(
                onClick = {
                    val nuevaAtraccion = Atraccion(
                        id = "0", // Ahora es String
                        nombre = nombre.trim(),
                        tipo = tipoSelected,
                        duracion = duracion.toInt(),
                        tiempoEspera = tiempoEspera.toInt(),
                        estado = estadoSelected,
                        imagen = imagen.trim().ifBlank { "https://res.cloudinary.com/djn8thk2s/image/upload/v1775453956/Logo_de_Parque_Aventura_xqo823.png"}
                    )
                    atraccionViewModel.insertar(nuevaAtraccion)
                },
                enabled = puedeGuardar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeAccent,
                    disabledContainerColor = VerdeAccent.copy(alpha = 0.4f)
                )
            ) {
                Text(text = "Guardar Atracción", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorCrear(onVolver: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = TextoPrincipal)
            }
        },
        title = { Text("Crear Atracción", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun SeccionFormulario(titulo: String, contenido: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = titulo, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        contenido()
    }
}

@Composable
fun ChipSeleccion(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    val fondo = if (seleccionado) VerdeAccent else Color.White
    val colorTexto = if (seleccionado) Color.White else TextoPrincipal
    Box(
        modifier = Modifier
            .border(1.5.dp, if (seleccionado) VerdeAccent else BordeInactivo, RoundedCornerShape(20.dp))
            .background(fondo, RoundedCornerShape(20.dp))
    ) {
        TextButton(onClick = onClick) {
            Text(text = texto, color = colorTexto)
        }
    }
}

@Composable
fun ChipEstado(texto: String, seleccionado: Boolean, colorActivo: Color, onClick: () -> Unit) {
    val fondo = if (seleccionado) colorActivo else Color.White
    val colorTexto = if (seleccionado) Color.White else TextoPrincipal
    Box(
        modifier = Modifier
            .border(1.5.dp, if (seleccionado) colorActivo else BordeInactivo, RoundedCornerShape(20.dp))
            .background(fondo, RoundedCornerShape(20.dp))
    ) {
        TextButton(onClick = onClick) {
            Text(text = texto, color = colorTexto)
        }
    }
}
