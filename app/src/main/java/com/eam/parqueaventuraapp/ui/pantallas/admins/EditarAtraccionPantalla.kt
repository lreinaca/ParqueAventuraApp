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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel

@Composable
fun PantallaEditarAtraccion(navController: NavController, viewModel: AtraccionViewModel, atraccionId: Int){
    //obtenemos la lista y buscamos la atracción con el id
    //firstOrNull devuelve null si no la encuentra
    val atracciones by viewModel.atracciones.collectAsState()
    val atraccion = atracciones.firstOrNull{it.id == atraccionId}

    //mientras la atracción no cargue se muestra un indicador
    if(atraccion == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = VerdeAccent)
        }
        return //salimos del composable y no se renderiza el formulario todavía
    }

    //formulario con valores iniciales q vienen de la atracción existente
    var nombre by remember { mutableStateOf(atraccion.nombre) }
    var tipoSelected by remember { mutableStateOf(atraccion.tipo) }
    var duracion by remember {mutableStateOf(atraccion.duracion.toFloat())}
    var tiempoEspera by remember {mutableStateOf(atraccion.tiempoEspera.toFloat())}
    var estadoSelected by remember { mutableStateOf(atraccion.estado) }
    var imagen by remember { mutableStateOf(atraccion.imagen) }

    //cuando la operación termina volvemos atrás (como en crearAtracciones)
    val operacionExito by viewModel.operacionExitosa.observeAsState()

    LaunchedEffect(operacionExito) {
        if (operacionExito == true){
            viewModel.resetOperacion()
            navController.popBackStack()
        }
    }

    // como en crear, no se puede guardar cambios si nombre está vacío
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
            // Nombre ---------------------------------------
            SeccionFormulario(titulo = "Nombre de la atracción") {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true //no permite saltos de linea, todo en una sola línea
                )
            }

            // Tipo de atracción ------------------------------
            //tres botones de selección
            SeccionFormulario(titulo = "Tipo de atracción") {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { //10dp de espacio entre elementos
                    listOf("familiar", "extrema", "infantil").forEach { tipo ->
                        // luego de recorrer la lista crea un chip por cada tipo
                        ChipSeleccion(
                            texto = tipo, //lo que muestra el chip
                            seleccionado = tipoSelected == tipo, //esto verifica si el chip está seleccionado
                            onClick = { tipoSelected = tipo } //si se selecciona, actualiza la variable
                        )
                    }
                }
            }

            // Duración del ciclo ------------------------------
            SeccionFormulario(titulo = "Duración del ciclo: ${duracion.toInt()} min") {
                Slider(
                    value = duracion,
                    onValueChange = { duracion = it },
                    valueRange = 1f..60f, //mínimo 1min y máximo 60min
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = VerdeAccent,
                        activeTrackColor = VerdeAccent
                    )
                )
                //etiqueta de los extremos del slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween //espacio entre elementos
                ) {
                    Text("1 min", fontSize = 11.sp, color = TextoSecundario)
                    Text("30 min", fontSize = 11.sp, color = TextoSecundario)
                    Text("60 min", fontSize = 11.sp, color = TextoSecundario)
                }
            }

            // Tiempo de espera -----------------------------
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0 min", fontSize = 11.sp, color = TextoSecundario)
                    Text("30 min", fontSize = 11.sp, color = TextoSecundario)
                    Text("60 min", fontSize = 11.sp, color = TextoSecundario)
                }
            }

            // Estado -------------------------------------
            SeccionFormulario(titulo = "Estado") {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                    //cada opción tiene su propio color cuando está seleccionada
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

            // URL de la imagen ---------------------------------
            SeccionFormulario(titulo = "URL de la imagen") {
                OutlinedTextField(
                    value = imagen,
                    onValueChange = {imagen = it},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Botón de guardar ---------------------------------
            Button(
                onClick = {
                    // Construimos la atracción actualizada
                    // Usamos copy() para conservar el ID original y solo cambiar los campos editados
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
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
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
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

//=======================================================================
// BARRA SUPERIOR -> muestra el nombre de la atracción como subtítulo
//=====================================================================
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
                // El subtítulo muestra el nombre de la atracción que se está editando
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