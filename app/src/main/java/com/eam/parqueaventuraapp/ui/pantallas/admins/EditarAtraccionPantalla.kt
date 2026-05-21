package com.eam.parqueaventuraapp.ui.pantallas.admins

import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel

@Composable
fun PantallaEditarAtraccion(navController: NavController, viewModel: AtraccionViewModel, atraccionId: String){
    LaunchedEffect(Unit) {
        viewModel.refrescar()
    }

    val atracciones by viewModel.atracciones.collectAsState()
    val atraccion = atracciones.firstOrNull{it.id == atraccionId}
    val mensajeOperacion by viewModel.mensajeOperacion.observeAsState()

    if(atraccion == null) {
        Scaffold(
            topBar = {
                BarraSuperiorEditar(
                    nombreAtraccion = "No disponible",
                    onVolver = { navController.popBackStack() }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "La atracción ya no existe en la API o no pudo cargarse.",
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    var nombre by remember { mutableStateOf(atraccion.nombre) }
    var tipoSelected by remember { mutableStateOf(atraccion.tipo) }
    var duracion by remember {mutableStateOf(atraccion.duracion.toFloat())}
    var tiempoEspera by remember {mutableStateOf(atraccion.tiempoEspera.toFloat())}
    var estadoSelected by remember { mutableStateOf(atraccion.estado) }
    var imagen by remember { mutableStateOf(atraccion.imagen) }

    val operacionExito by viewModel.operacionExitosa.observeAsState()

    LaunchedEffect(operacionExito) {
        if (operacionExito == true){
            viewModel.resetOperacion()
            navController.popBackStack()
        }
    }

    val puedeGuardar = nombre.isNotBlank()

    Scaffold(
        topBar = {
            BarraSuperiorEditar(
                nombreAtraccion = atraccion.nombre,
                onVolver = {navController.popBackStack()}
            )
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
                    onValueChange = {imagen = it},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Button(
                onClick = {
                    val actualizada = atraccion.copy(
                        nombre = nombre.trim(),
                        tipo = tipoSelected,
                        duracion = duracion.toInt(),
                        tiempoEspera = tiempoEspera.toInt(),
                        estado = estadoSelected,
                        imagen = imagen.trim().ifBlank { atraccion.imagen }
                    )
                    viewModel.actualizar(actualizada)
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
                Text(
                    text = "Guardar Cambios",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            if (mensajeOperacion != null) {
                Text(
                    text = mensajeOperacion!!,
                    color = Color(0xFFCD3735),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorEditar(
    nombreAtraccion: String,
    onVolver: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoPrincipal
                )
            }
        },
        title = {
            Column {
                Text(
                    text = "Editar Atracción",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextoPrincipal
                )
                Text(
                    text = nombreAtraccion,
                    fontSize = 13.sp,
                    color = TextoSecundario
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}
