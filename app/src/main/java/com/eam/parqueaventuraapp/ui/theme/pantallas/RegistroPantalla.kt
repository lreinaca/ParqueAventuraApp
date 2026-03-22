package com.eam.parqueaventuraapp.ui.theme.pantallas

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eam.parqueaventuraapp.ui.theme.viewModel.UsuarioViewModel

@Composable
fun PantallaRegistro(navController: NavController, viewModel: UsuarioViewModel) {
    // Definición de estados para los campos de texto usando remember para mantener el valor durante recomposiciones
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    
    // Obtenemos el contexto actual para mostrar mensajes (Toast)
    val contexto = LocalContext.current

    // Contenedor principal organizado en una columna centrada
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título de la pantalla
        Text(text = "Registro", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        
        // Espaciador vertical
        Spacer(modifier = Modifier.height(32.dp))

        // Campo de entrada para el nombre completo
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada para el correo electrónico
        TextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada para la contraseña con transformación visual para ocultar caracteres
        TextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Botón para procesar el registro
        Button(
            onClick = {
                // Validación: verificamos que ningún campo esté vacío
                if (nombre.isNotEmpty() && correo.isNotEmpty() && clave.isNotEmpty()) {
                    // Llamamos a la función de registro del ViewModel
                    viewModel.registro(nombre, correo, clave)
                    
                    // Mensaje de confirmación al usuario
                    Toast.makeText(contexto, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                    
                    // Navegamos hacia atrás en la pila (vuelve al login)
                    navController.popBackStack()
                } else {
                    // Mensaje de advertencia si faltan datos
                    Toast.makeText(contexto, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Cuenta")
        }

        // Enlace para navegar de regreso si el usuario ya tiene una cuenta
        TextButton(onClick = { navController.popBackStack() }) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}
