package com.eam.parqueaventuraapp.ui.pantallas.usuarios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
import com.eam.parqueaventuraapp.ui.componentes.BarraNavegacionInferior
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

@Composable
fun PantallaInicio(navController: NavController, usuarioViewModel: UsuarioViewModel, atraccionViewModel: AtraccionViewModel) {
    val usuario by usuarioViewModel.usuarioActual.collectAsState()
    val todasLasAtracciones by atraccionViewModel.atracciones.collectAsState()

    // FILTRO CLAVE: Solo mostramos lo que NO sea INACTIVA
    val atraccionesVisibles = todasLasAtracciones.filter { it.estado != "INACTIVA" }
    
    val totalAtracciones = atraccionesVisibles.size
    val disponibles = atraccionesVisibles.count { it.estado == "ABIERTA" }
    val atraccionesAbiertas = atraccionesVisibles.filter { it.estado == "ABIERTA" }

    Scaffold(
        bottomBar = { BarraNavegacionInferior(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantalla),
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
        ) {
            item {
                CabeceraHome(
                    nombreUsuario = usuario?.nombre ?: "Usuario",
                    totalAtracciones = totalAtracciones,
                    disponibles = disponibles
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Atracciones disponibles", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextoPrincipal)
                    TextButton(onClick = { navController.navigate("atracciones") }) {
                        Text(text = "Ver todas >", color = VerdeAccent, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(atraccionesAbiertas) { atraccion ->
                ItemAtraccion(
                    atraccion = atraccion,
                    onVerDetalle = { navController.navigate("detalle/${atraccion.id}") }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun CabeceraHome(nombreUsuario: String, totalAtracciones: Int, disponibles: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerdeOscuro, shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(50.dp).background(VerdeAccent, CircleShape), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = "https://res.cloudinary.com/djn8thk2s/image/upload/v1775453956/Logo_de_Parque_Aventura_xqo823.png",
                            contentDescription = "logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Aventura Park", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("¡Hola!", color = VerdeAccent, fontWeight = FontWeight.SemiBold)
            Text(text = nombreUsuario, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            Text(text = "Prepárate para vivir un día lleno de aventuras", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                TarjetaContador(titulo = "Atracciones", valor = totalAtracciones.toString(), modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(10.dp))
                TarjetaContador(titulo = "Disponibles", valor = disponibles.toString(), modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun TarjetaContador(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp)).padding(16.dp)) {
        Column {
            Text(text = valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(text = titulo, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

@Composable
fun ItemAtraccion(atraccion: Atraccion, onVerDetalle: () -> Unit) {
    Card(
        onClick = onVerDetalle,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(100.dp)) {
            AsyncImage(
                model = atraccion.imagen,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(100.dp).fillMaxHeight().clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
            )
            Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                Text(text = atraccion.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextoPrincipal)
                Text(text = "${atraccion.tipo} • ${atraccion.tiempoEspera} min", fontSize = 13.sp, color = TextoSecundario)
            }
        }
    }
}
