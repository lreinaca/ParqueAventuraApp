package com.eam.parqueaventuraapp.ui.pantallas.admins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.eam.parqueaventuraapp.data.modelo.Atraccion
import com.eam.parqueaventuraapp.ui.theme.*
import androidx.compose.foundation.layout.IntrinsicSize
import com.eam.parqueaventuraapp.ui.viewModel.AtraccionViewModel
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

// ===============================================================
// PANTALLA PANEL ADMINISTRADOR
// ==============================================================
@Composable
fun PantallaInicioAdmin(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel,
    atraccionViewModel: AtraccionViewModel
){

    // observeAsState() "escucha" el LiveData del ViewModel
    val atracciones by atraccionViewModel.atracciones.collectAsState()
    val usuarios by usuarioViewModel.usuarios.collectAsState()

    //contadores para mostrar en la cabecera
    val totalAtracciones = atracciones.size
    val totalUsuarios = usuarios.size
    val activas = atracciones.count {it.estado == "ABIERTA"}
    val mantenimientos = atracciones.count {it.estado == "MANTENIMIENTO"}
    val cerradas = atracciones.count {it.estado == "CERRADA"}

    //las atracciones "recientes", las ultimas 3 insertadas a la app
    // como el id es autoincremental, un id más alto significa que fue creada después
    val recientes = atracciones.sortedByDescending { it.id }.take(3)

    Scaffold { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantalla),
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())
        ) {

            //cabecera verde superior
            item {
                CabeceraAdmin(
                    navController = navController,
                    usuarioViewModel = usuarioViewModel,
                    totalAtracciones = totalAtracciones,
                    totalUsuarios = totalUsuarios
                )
            }

            //tarjetas de estado (Activas / Mantenimiento / Cerradas)
            item {
                Spacer(modifier = Modifier.height(16.dp)) //espacio entre cabecera y tarjetas
                FilaEstados(activas = activas, mantenimiento = mantenimientos, cerradas = cerradas)
            }

            // acciones rápidas
            item {
                Spacer(modifier = Modifier.height(20.dp)) //espacio entre tarjetas y acciones rápidas
                Text(
                    text = "Acciones rápidas",
                    fontWeight = FontWeight.Bold, //negrita
                    fontSize = 18.sp,
                    color = TextoPrincipal,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(12.dp)) //espacio entre acciones rápidas y atracciones recientes
                SeccionAccionesRapidas(navController = navController)
            }

            //título de atracciones recientes
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Atracciones recientes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextoPrincipal,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            //lista de las últimas 3 atracciones creadas
            items(recientes) { atraccion ->
                ItemAtraccionReciente(atraccion = atraccion) //cada atraccion es un item de la lista
                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp)) //linea separadora entre atracciones
            }
            item { Spacer(modifier = Modifier.height(20.dp)) } //espacio al final de la lista
        }
    }
}

// =============================================================
// CABECERA VERDE SUPERIOR
// ===============================================================
@Composable
fun CabeceraAdmin(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel,
    totalAtracciones: Int,
    totalUsuarios: Int
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                VerdeOscuro,
                RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .padding(20.dp)
    )
    {
        Column {

            //fila supertior: fecha <- + título
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.spacedBy(12.dp)
            ) {
                //botón de volver a el login
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape), //fondo blanco con opacidad
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            usuarioViewModel.cerrarSesion()
                            navController.navigate("login"){
                            popUpTo(0) {inclusive = true } // 0 -> borra todas las pantallas de la pila
                        } },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription ="Volver",
                            tint = Color.White
                        )
                    }
                }

                //título superior
                Column{
                    Text(
                        text = "Panel Administrador",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Parque Aventura",
                        color = Color.White.copy(alpha = 0.7f), //le agrega opacidad
                        fontSize = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            //tarjeta de contadores (total atracciones y usuarios)
            Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                TarjetaContadorAdmin(
                    icono = Icons.Default.LocalActivity,
                    valor = totalAtracciones.toString(),
                    titulo = "Total\nAtracciones",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                TarjetaContadorAdmin(
                    icono = Icons.Default.Group,
                    valor = totalUsuarios.toString(),
                    titulo = "Usuarios",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// ==============================================================================
// TARJETA DE CONTADOR DE LA CABECERA (mismo concepto que en PantallaInicio de useers)
// ===================================================================================
@Composable
fun TarjetaContadorAdmin(
    icono: ImageVector,
    valor: String,
    titulo: String,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier
            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        //circulo con icono
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(VerdeAccent, CircleShape),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Column {
            Text(
                text = valor,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            Text(text = titulo,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}

// ===============================================================
// FILA DE ESTADOS (Activas / Mantenimiento /Cerradas)
// ================================================================
@Composable
fun FilaEstados(activas: Int, mantenimiento: Int, cerradas: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        //ACTIVAS
        TarjetaEstado(
            valor = activas.toString(),
            etiqueta = "Activas",
            color = Color(0xFF4caf50),
            modifier = Modifier.weight(1f)
        )
        //MANTENIMIENTO
        TarjetaEstado(
            valor = mantenimiento.toString(),
            etiqueta = "Mantenimiento",
            color = Color(0xFFff9800),
            modifier = Modifier.weight(1f)
        )
        //CERRADAS
        TarjetaEstado(
            valor = cerradas.toString(),
            etiqueta = "Cerradas",
            color = Color(0xFF9e9e9e),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TarjetaEstado(valor: String, etiqueta: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) //sombra
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //punto de color que identifica el estado
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )
            Column {
                Text(
                    text = valor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextoPrincipal
                )
                Text(
                    text = etiqueta,
                    fontSize = 11.sp,
                    color = TextoSecundario
                )
            }
        }
    }
}

//===============================================================
// SECCIÓN DE ACCIONES RÁPIDAS
// ========================================================
@Composable
fun SeccionAccionesRapidas(navController: NavController) {
    //primera fila donde está crear atracciones y gestionar atracciones ----------------------------
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BotonAccionRapida(
            icono = Icons.Default.Add,
            etiqueta = "Crear\nAtracción",
            color = Color(0xFF4CAF50), // Verde
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate("crearAtraccion") }
        )
        BotonAccionRapida(
            icono = Icons.Default.List,
            etiqueta = "Gestionar\nAtracciones",
            color = Color(0xFF2196F3), // Azul
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate("gestionAtracciones") }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    //segunda fila donde está gestionar usuarios ---------------------------------------------------
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        BotonAccionRapida(
            icono = Icons.Default.Group,
            etiqueta = "Gestionar\nUsuarios",
            color = Color(0xFFFF6B2B), // Naranja
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate("admin_usuarios") }
        )
        // Este Spacer ocupa la otra mitad (derecha) dejando el botón a la izquierda
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun BotonAccionRapida(
    icono: ImageVector,
    etiqueta: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Card(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween

        ) {
            //icono en un circulo semitransparente
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint= Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = etiqueta,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

// ===============================================================
// ITEM DE ATRACCIÓN RECIENTE
// ==========================================================
@Composable
fun ItemAtraccionReciente(atraccion: Atraccion) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        //imagen de la atracción redonda
        AsyncImage(
            model = atraccion.imagen,
            contentDescription = atraccion.nombre,
            contentScale = ContentScale.Crop, //ajusta la imagen al tamaño del contenedor
            modifier = Modifier //tamaño del circulo
                .size(48.dp)
                .clip(CircleShape)
        )

        //Nombre y tiempo de espera
        Column(modifier = Modifier.weight(1f)) {
            Text(text = atraccion.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextoPrincipal)
            Text(text = "${atraccion.tiempoEspera} min de espera", fontSize = 12.sp, color = TextoSecundario)
        }
        BadgeEstadoAdmin(estado = atraccion.estado) //se muestra a la derecha
    }
}

// BADGE DE ESTADO (Activa / Cerrada / Mantenimiento)
@Composable
fun BadgeEstadoAdmin(estado: String) {
    val (texto, fondo, colorTexto) = when (estado.uppercase()) {
        "ABIERTA" -> Triple("Activa", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "CERRADA" -> Triple("Cerrada", Color(0xFFFFEBEE), Color(0xFFC62828))
        "MANTENIMIENTO" -> Triple("Mantenimiento", Color(0xFFFFF8E1), Color(0xFFF57F17))
        else -> Triple(estado, Color(0xFFF5F5F5), TextoSecundario)
    }
    Box(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text = texto, color = colorTexto, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}