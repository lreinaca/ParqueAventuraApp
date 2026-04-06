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

// =============================================================
// PANTALLA AÑADIR NUEVAS ATRACCIONES
// ==============================================================
@Composable
fun PantallaCrearAtraccion(navController: NavController, atraccionViewModel: AtraccionViewModel) {
    //estado inicial de cada campo del formulario
    //remember + matableStateOf = el valor se recuerda entre recomposiciones
    var nombre by remember { mutableStateOf("") }
    var tipoSelected by remember { mutableStateOf("familiar") } //por defecto está seleccionado "familiar"
    var duracion by remember { mutableStateOf(5f) } //slider devuelve Float
    var tiempoEspera by remember { mutableStateOf(15f) }
    var estadoSelected by remember { mutableStateOf("ABIERTA") } //por defecto también
    var imagen by remember { mutableStateOf("") }

    //observamos el resultado de la operación para ver si se guardó correctamente
    val operacionExito by atraccionViewModel.operacionExitosa.observeAsState()

    //launcherEffect se ejecuta cuando operaciónExito cambia
    // si se guardó bien (con true) entonces nos devuelve a la pantalla anterior
    LaunchedEffect(operacionExito) {
        if (operacionExito == true) { //si se guardó
            atraccionViewModel.resetOperacion() //limpiamos el estado
            navController.popBackStack() //volvemos atrás
        }
    }

    //el botón guardar solo se activa si el campo nombre no está vacío
    val puedeGuardar = nombre.isNotBlank()

    Scaffold(
        topBar = {
            BarraSuperiorCrear(onVolver = { navController.popBackStack() })
        }
    ) { padding ->

        //columna con scroll vertical para que el formulario se vea en pantallas pequeñas
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantalla)
                .padding(padding)
                .verticalScroll(rememberScrollState()) //scroll vertical
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp) //espacio entre elementos
        ) {
            // Nombre ---------------------------------------
            SeccionFormulario(titulo = "Nombre de la atracción") {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Ej: Torre del Terror", color = TextoSecundario) },
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
                            onClick = {
                                tipoSelected = tipo
                            } //si se selecciona, actualiza la variable
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
                    placeholder = {Text("https://...", color = TextoSecundario)},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Botón guardar -----------------------------------
            Button(
                onClick = {
                    //creamos el objeto atracción con los datos del formulario
                    val nuevaAtraccion = Atraccion(
                        id = 0, // Room lo genera automáticamente
                        nombre = nombre.trim(),
                        tipo = tipoSelected,
                        duracion = duracion.toInt(),
                        tiempoEspera = tiempoEspera.toInt(),
                        estado = estadoSelected,
                        imagen = imagen.trim().ifBlank { "https://picsum.photos/id/237/200/300"}
                    )
                    atraccionViewModel.insertar(nuevaAtraccion)
                },
                enabled = puedeGuardar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp), //redondeamos los bordes
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeAccent,
                    disabledContainerColor = VerdeAccent.copy(alpha = 0.4f)
                )
            ) {
                Text(
                    text = "Guardar Atracción",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ===============================================================
// BARRA SUPERIOR título + botón volver
// =============================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorCrear(onVolver: () -> Unit) {
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
                    text = "Crear Atracción",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextoPrincipal
                )
                Text(
                    text = "Completa el formulario",
                    fontSize = 13.sp,
                    color = TextoSecundario
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

// ===============================================================
//  CONTENEDOR DE SECCIÓN
//=============================================================
@Composable
//se le puede pasar cualquier composable, esto evita repetir el título y espaciado en cada sección
fun SeccionFormulario(titulo: String, contenido: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = titulo,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = TextoPrincipal
        )
        contenido()
    }
}

//==============================================================
// CHIP DE SELECCIÓN DEL TIPO (familiar/extrema/infantil)
// ===============================================================
@Composable
fun ChipSeleccion(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    // Si está seleccionado: fondo verde + texto blanco.
    // Si no: fondo blanco + borde gris + texto oscuro.
    val fondo = if (seleccionado) VerdeAccent else Color.White
    val colorTexto = if (seleccionado) Color.White else TextoPrincipal

    Box(
        modifier = Modifier
            .border(
                width = 1.5.dp,
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


// ===============================================================
// CHIP DE ESTADO (activa/mantenimiento/cerrada)
// ===============================================================
@Composable
fun ChipEstado(texto: String, seleccionado: Boolean, colorActivo: Color, onClick: () -> Unit) {
    val fondo = if (seleccionado) colorActivo else Color.White
    val colorTexto = if (seleccionado) Color.White else TextoPrincipal

    Box(
        modifier = Modifier
            .border(
                width = 1.5.dp,
                color = if (seleccionado) colorActivo else BordeInactivo,
                shape = RoundedCornerShape(20.dp)
            )
            .background(fondo,RoundedCornerShape(20.dp))
    ) {
        TextButton(onClick = onClick) {
            Text(
                text = texto,
                color = colorTexto,
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                fontSize = 13.sp
            )
        }
    }
}