package com.eam.parqueaventuraapp.ui.theme.pantallas

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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.ui.theme.viewModel.UsuarioViewModel

@Composable
fun PantallaLogin(navController: NavController, viewModel: UsuarioViewModel) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var claveVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val loginStatus by viewModel.loginStatus.observeAsState()

    LaunchedEffect(loginStatus) {
        if (loginStatus == true) {
            Toast.makeText(context, "¡Bienvenido de nuevo!", Toast.LENGTH_SHORT).show()
        } else if (loginStatus == false) {
            Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }

    val darkBg = Color(0xFF0D1B2A)
    val accentGreen = Color(0xFF1EF036)
    val lightGrayText = Color(0xFF99A1AF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Icono de Rayo (Usando un icono disponible por defecto para evitar errores de compilación)
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(accentGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send, // Cambiado temporalmente para evitar errores
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

        // Tarjeta Blanca
        Surface(
            modifier = Modifier
                .fillMaxSize(),
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
                    color = Color.Black
                )
                Text(
                    text = "Inicia sesión para continuar",
                    fontSize = 14.sp,
                    color = lightGrayText
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Campo Correo
                Text("Correo electrónico", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    placeholder = { Text("demo@aventurapark.com", color = lightGrayText) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = lightGrayText) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE5E7EB),
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Contraseña
                Text("Contraseña", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = clave,
                    onValueChange = { clave = it },
                    placeholder = { Text("demo123", color = lightGrayText) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = lightGrayText) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE5E7EB),
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = accentGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.End).clickable { /* Acción */ }
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Botón Iniciar Sesión
                Button(
                    onClick = { viewModel.inicioSesion(correo, clave) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Iniciar Sesión", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Texto de Registro
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = buildAnnotatedString {
                            append("¿No tienes cuenta? ")
                            withStyle(style = SpanStyle(color = accentGreen, fontWeight = FontWeight.Bold)) {
                                append("Regístrate")
                            }
                        },
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.clickable { navController.navigate("registro") }
                    )
                }
            }
        }
    }
}
