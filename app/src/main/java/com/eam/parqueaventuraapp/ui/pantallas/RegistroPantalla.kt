package com.eam.parqueaventuraapp.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.ui.theme.*
import com.eam.parqueaventuraapp.ui.viewModel.UsuarioViewModel

@Composable
fun PantallaRegistro(navController: NavController, viewModel: UsuarioViewModel) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var claveVisible by remember { mutableStateOf(false) }
    
    val contexto = LocalContext.current
    val scrollState = rememberScrollState()

    // Observamos el estado del login/registro desde el ViewModel
    val loginStatus by viewModel.loginStatus.observeAsState()

    LaunchedEffect(loginStatus) {
        if (loginStatus == true) {
            Toast.makeText(contexto, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } else if (loginStatus == false) {
            Toast.makeText(contexto, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = TextoPrincipal)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Crear cuenta", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
        Text("Únete a la aventura hoy mismo", fontSize = 16.sp, color = TextoSecundario)
        Spacer(modifier = Modifier.height(40.dp))

        // Campos de texto...
        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = correo, onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = clave, onValueChange = { clave = it },
            label = { Text("Contraseña") },
            visualTransformation = if (claveVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { 
                // LA LÓGICA DE VALIDACIÓN AHORA ESTÁ EN EL VIEWMODEL
                viewModel.registro(nombre, correo, clave) 
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeAccent)
        ) {
            Text("Crear Cuenta", fontWeight = FontWeight.Bold)
        }
    }
}
