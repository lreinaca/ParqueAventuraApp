package com.eam.parqueaventuraapp.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.data.modelo.Roles
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

@Composable
fun PantallaLogin(navController: NavController, viewModel: UsuarioViewModel) {
    val context = LocalContext.current
    
    // Obtenemos las credenciales recordadas del ViewModel
    val credenciales by viewModel.credencialesRecordadas.collectAsState()
    
    // Estados locales para los campos de texto
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    // Paso 4: Cargar datos al iniciar invocando al ViewModel
    LaunchedEffect(Unit) {
        viewModel.cargarCredenciales(context)
    }

    // Actualizar campos cuando cambien las credenciales en el ViewModel
    LaunchedEffect(credenciales) {
        correo = credenciales.first
        clave = credenciales.second
    }

    val loginStatus by viewModel.loginStatus.observeAsState()
    val usuarioActual by viewModel.usuarioActual.collectAsState()

    LaunchedEffect(loginStatus) {
        if (loginStatus == true && usuarioActual != null) {
            Toast.makeText(context, "¡Bienvenido de nuevo, ${usuarioActual?.nombre}!", Toast.LENGTH_SHORT).show()
            
            if (usuarioActual?.rol == Roles.ADMIN) {
                navController.navigate("panelAdmin") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                navController.navigate("inicioUsuario") {
                    popUpTo("login") { inclusive = true }
                }
            }
        } else if (loginStatus == false) {
            Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }

    val darkBg = Color(0xFF0D1B2A)
    val accentGreen = Color(0xFF1EF036)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Box(
            modifier = Modifier
                .size(60.dp)
                .background(accentGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Parque Aventura",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Vive la experiencia más emocionante",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "¡Bienvenido!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
                Text(
                    text = "Inicia sesión para continuar",
                    fontSize = 14.sp,
                    color = TextoSecundario
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text("Correo electrónico", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    placeholder = { Text("demo@aventurapark.com", color = TextoSecundario) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TextoSecundario) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Contraseña", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = clave,
                    onValueChange = { clave = it },
                    placeholder = { Text("demo123", color = TextoSecundario) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TextoSecundario) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = { 
                        // El ViewModel ahora maneja el login y el guardado de credenciales
                        viewModel.inicioSesion(context, correo, clave)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Iniciar Sesión", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = buildAnnotatedString {
                            append("¿No tienes cuenta? ")
                            withStyle(style = SpanStyle(color = accentGreen, fontWeight = FontWeight.Bold)) {
                                append("Regístrate")
                            }
                        },
                        modifier = Modifier.clickable { navController.navigate("registro") },
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}
