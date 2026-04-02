package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.eam.parqueaventuraapp.R
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import com.eam.parqueaventuraapp.ui.componentes.BarraNavegacionInferior
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

// ===============================================================
// PANTALLA PRINCIPAL DE USUARIOS
// =============================================================
@Composable
fun PantallaInicio(navController: NavController, usuarioViewModel: UsuarioViewModel, atraccionViewModel: AtraccionViewModel) {

    // observeAsState() "escucha" el LiveData del ViewModel
    val usuario by usuarioViewModel.usuarioActual.collectAsState()
    val atracciones by atraccionViewModel.atracciones.collectAsState()

    // calculo de algunos datos para mostrar en la cabecera
    val totalAtracciones = atracciones.size
    val disponibles = atracciones.count { it.estado == "ABIERTA" }

    //filtramos la lista para que en inicio solo se muestren las que están abiertas
    val atraccionesAbiertas = atracciones.filter { it.estado == "ABIERTA" }

    Scaffold(
        bottomBar = {
            BarraNavegacionInferior(navController)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantalla),
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
        ) {

            // item { } = un elemento único que NO se repite
            item {
                CabeceraHome(
                    nombreUsuario = usuario?.nombre ?: "Usuario",
                    totalAtracciones = totalAtracciones,
                    disponibles = disponibles
                )
            }

            //encabezado de la sección con el link "ver todas >"
            item {
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Atracciones disponibles",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    // TextButton es el "Ver todas >" —texto clickeable sin borde (ccomo si fuera link de html)
                    //al darle clic navega a la pantalla de atracciones (catalogo completo)
                    TextButton(onClick = { navController.navigate("atracciones") }) {
                        Text(
                            text = "Ver todas >",
                            color = VerdeAccent,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // Lista de atracciones abiertas
            items(atraccionesAbiertas) { atraccion ->
                ItemAtraccion(
                    atraccion = atraccion,
                    onVerDetalle = { navController.navigate("detalle/${atraccion.id}") }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// =============================================================
// CABECERA VERDE SUPERIOR
// ========================?======================================
@Composable
fun CabeceraHome(
    nombreUsuario: String,
    totalAtracciones: Int,
    disponibles: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                VerdeOscuro,
                shape = RoundedCornerShape(
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                ) // Bordes redondeados
            )
            .padding(20.dp)
    ) {
        Column {

            // fila superior: logo + nombre de la app + notificaciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // Círculo verde con ícono (logo simple)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(VerdeAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img),
                            contentDescription = "logo",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Aventura Park",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Saludo con el nombre del usuario logueado ---
            Text("¡Hola!", color = VerdeAccent, fontWeight = FontWeight.SemiBold)
            Text(
                text = nombreUsuario,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
            Text(
                text = "Prepárate para vivir un día lleno de aventuras",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- Tarjetas de contadores ---
            Row {
                TarjetaContador(
                    titulo = "Atracciones",
                    valor = totalAtracciones.toString(),
                    modifier = Modifier.weight(1f)   // ocupa la mitad del ancho disponible
                )
                Spacer(modifier = Modifier.width(10.dp))
                TarjetaContador(
                    titulo = "Disponibles",
                    valor = disponibles.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


// ==================================================================
// TARJETA DE CONTADOR (Atracciones / Disponibles)
// ===============================================================
@Composable
fun TarjetaContador(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(text = valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(text = titulo, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

// ==============================================================
// ITEM DE LA LISTA VERTICAL (LazyColumn)
// ===================================================================
@Composable
fun ItemAtraccion(
    atraccion: Atraccion,
    onVerDetalle: () -> Unit
) {
    var favorito by remember { mutableStateOf(false) }

    Card(
        onClick = onVerDetalle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(100.dp)) {

            // --- Imagen izquierda con badge encima ---
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model = atraccion.imagen,
                    contentDescription = atraccion.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                )

                BadgeCategoria(
                    tipo = atraccion.tipo,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                )
            }

            // --- Información: nombre, descripción, badges ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                // Fila superior: nombre + corazón
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = atraccion.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(
                        onClick = { favorito = !favorito },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (favorito) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (favorito) Color.Red else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Descripción breve usando los campos del modelo
                Text(
                    text = "Duración: ${atraccion.duracion} min · ${atraccion.tipo}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Spacer con weight(1f) actúa como un resorte:
                // empuja los badges hacia la parte inferior de la tarjeta
                Spacer(modifier = Modifier.weight(1f))

                // Fila inferior: tiempo de espera + estado
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BadgeTiempo(minutos = atraccion.tiempoEspera)
                    BadgeEstado(estado = atraccion.estado)
                }
            }
        }
    }
}


// ===============================================================
// BADGE DE CATEGORÍA (Infantil / Extrema / Familiar)
// ===============================================================
@Composable
fun BadgeCategoria(tipo: String, modifier: Modifier = Modifier) {

    // when = estructura de control equivalente a switch/case en Java
    val color = when (tipo.lowercase()) {
        "infantil" -> ColorInfantil
        "extrema" -> ColorExtrema
        "familiar" -> ColorFamiliar
        else -> Color.Gray
    }

    Box(
        modifier = modifier
            .background(color, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = tipo.replaceFirstChar { it.uppercase() }, // primera letra en mayúscula
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ============================================================
// BADGE DE TIEMPO DE ESPERA (píldora verde con ícono de reloj)
// ===============================================================
@Composable
fun BadgeTiempo(minutos: Int) {
    Row(
        modifier = Modifier
            .background(Color(0xFFE8F5E9), RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "⏱", fontSize = 11.sp)
        Text(
            text = "$minutos min",
            color = VerdeClaro,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


// ===============================================================
// BADGE DE ESTADO (Activa / Cerrada / Mantenimiento)
// ===================================================
@Composable
fun BadgeEstado(estado: String) {

    // Triple() devuelve 3 valores a la vez: texto visible, color de fondo y color del texto.
    // Esto evita tener 3 bloques when separados.
    val (texto, fondo, colorTexto) = when (estado.uppercase()) {
        "ABIERTA" -> Triple("Activa", Color(0xFFE8F5E9), VerdeClaro)
        "CERRADA" -> Triple("Cerrada", Color(0xFFFFEBEE), Color(0xFFC62828))
        "MANTENIMIENTO" -> Triple("Mantenimiento", Color(0xFFFFF8E1), Color(0xFFF57F17))
        else -> Triple(estado, Color(0xFFF5F5F5), Color.Gray)
    }

    Box(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = texto, color = colorTexto, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
