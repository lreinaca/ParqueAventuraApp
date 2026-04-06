package com.eam.parqueaventuraapp.ui.pantallas.admins

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.eam.parqueaventuraapp.data.modelo.Atraccion

// ============================================================
// PANTALLA GESTIÓN DE ATRACCIONES (para administradores)
// ==============================================================
@Composable
fun PantallaGestionAtracciones(navController: NavController, atraccionViewModel: AtraccionViewModel) {
    val atracciones by atraccionViewModel.atracciones.collectAsState()

    Scaffold(
        //barra superior con el título, botón atrás y botón para añadir una nueva atracción
        topBar = {
            BarraSuperiorAtracciones(
                totalAtracciones = atracciones.size,
                onVolver = {navController.popBackStack()},
                onCrear = {navController.navigate("crearAtraccion")}
            )
        }
    ) { padding ->

        //cuando aún no hayan atracciones se muestra un texto en la mitad
        if(atracciones.isEmpty()){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center //centrado
            ) {
                Text(text = "No hay atracciones registradas aún", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FondoPantalla),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 12.dp,
                    bottom = padding.calculateBottomPadding() + 12.dp
                )
            ) {
                items(atracciones){ atraccion ->
                    ItemAtraccionAdmin(
                        atraccion = atraccion,
                        onEditar = {navController.navigate("editarAtraccion/${atraccion.id}")}
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

// ===============================================================
// Barra superior
// ==========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorAtracciones(
    totalAtracciones : Int,
    onVolver : () -> Unit, // Unit significa que no devuelve nada
    onCrear : () -> Unit
){
    TopAppBar(
        //BOTÓN VOLVER -----------
        navigationIcon = {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }
        },

        //TÍTULO Y SUBTITULO -----------
        title = {
            Column {
                Text(
                    text = "Gestionar Atracciones",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "$totalAtracciones atracciones",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        },

        //BOTÓN "+" AÑADIR NUEVA ATRACCIÓN
        actions = { //
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(40.dp)
                    .background(VerdeAccent, CircleShape),
                contentAlignment = Alignment.Center
            ){
                IconButton(onClick = onCrear) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Ir a crear atracción",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

// ==============================================================
// ITEM PARA CADA ATRACCIÓN EN LA LISTA
// ============================================================
@Composable
fun ItemAtraccionAdmin(
    atraccion: Atraccion,
    onEditar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) //sombra
    ) {
        Row (
            modifier          = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            //imagen de la atracción
            AsyncImage(
                model = atraccion.imagen,
                contentDescription = atraccion.nombre,
                contentScale = ContentScale.Crop, //ajusta la imagen al tamaño del contenedor
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            //info de la atracción: nombre, tipo y tiempo de espera
            Column(modifier = Modifier.weight(1f)){
                Text(
                    text = atraccion.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "${atraccion.tipo} · ${atraccion.tiempoEspera} min espera", //concatena ambos datos
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⏱ ${atraccion.duracion} min / ciclo",
                    fontSize = 12.sp,
                    color = Color(0xFFB3590A) // naranja
                )
            }

            //columna derecha donde están las insignias/badges que muestran el estado + botón para eidtar
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BadgeEstadoGestion(estado = atraccion.estado)

                //botón editar
                OutlinedButton(
                    onClick = onEditar,
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = VerdeClaro
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Editar",
                        fontSize = 12.sp,
                        color = VerdeClaro
                    )
                }
            }
        }
    }
}

// ===============================================================
// BADGE DE ESTADO
// ============================================================
@Composable
fun BadgeEstadoGestion(estado: String){
    //es como un switch case de Javaa
    val (texto, fondo, colorTexto, colorPunto) = when (estado.uppercase()) {
        "ABIERTA" -> Quad("Activa", Color(0xFFE8F5E9), Color(0xFF2E7D32), Color(0xFF4CAF50))
        "CERRADA" -> Quad("Cerrada", Color(0xFFFFEBEE), Color(0xFFC62828), Color(0xFFE53935))
        "MANTENIMIENTO" -> Quad("Mantenimiento", Color(0xFFFFF3E0), Color(0xFFE65100), Color(0xFFFF9800))
        else -> Quad(estado, Color(0xFFF5F5F5), Color.Gray, Color.Gray)
    }

    Row(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(20.dp)) //fondo con las esquinas redondeadas
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        //punto de color a la izquierda del texto
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(colorPunto, CircleShape)
        )
        Text(
            text = texto,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorTexto
        )
    }
}

//quad es como Triple (que usamos en DashboardAdminPantalla) pero para 4 valores
//lo usamos solo aquí para no tener 4 variables separadas en el when
private data class Quad(
    val first: String,
    val second: Color,
    val third: Color,
    val fourth: Color
)