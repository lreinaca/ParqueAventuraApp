package com.eam.parqueaventuraapp.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.eam.parqueaventuraapp.ui.theme.AccentGreen
import com.eam.parqueaventuraapp.ui.theme.LightGrayText
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Regresar",
                modifier = Modifier.size(28.dp),
                tint = TextoPrincipal
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Crear cuenta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = TextoPrincipal
        )
        
        Text(
            text = "Únete a la aventura hoy mismo",
            fontSize = 16.sp,
            color = TextoSecundario
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campo Nombre completo
        Text(
            text = "Nombre completo",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = TextoPrincipal
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Tu nombre", color = TextoSecundario) },
            leadingIcon = { 
                Icon(
                    imageVector = Icons.Default.Person, 
                    contentDescription = null, 
                    tint = TextoSecundario,
                    modifier = Modifier.size(20.dp)
                ) 
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = TextoPrincipal,
                unfocusedTextColor = TextoPrincipal
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo Correo electrónico
        Text(
            text = "Correo electrónico",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = TextoPrincipal
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            placeholder = { Text("tu@email.com", color = TextoSecundario) },
            leadingIcon = { 
                Icon(
                    imageVector = Icons.Default.Email, 
                    contentDescription = null, 
                    tint = TextoSecundario,
                    modifier = Modifier.size(20.dp)
                ) 
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = TextoPrincipal,
                unfocusedTextColor = TextoPrincipal
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo Contraseña
        Text(
            text = "Contraseña",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = TextoPrincipal
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            placeholder = { Text("Mínimo 6 caracteres", color = TextoSecundario) },
            leadingIcon = { 
                Icon(
                    imageVector = Icons.Default.Lock, 
                    contentDescription = null, 
                    tint = TextoSecundario,
                    modifier = Modifier.size(20.dp)
                ) 
            },
            trailingIcon = {
                val imagen = if (claveVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { claveVisible = !claveVisible }) {
                    Icon(
                        imageVector = imagen, 
                        contentDescription = null, 
                        tint = TextoSecundario,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (claveVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = TextoPrincipal,
                unfocusedTextColor = TextoPrincipal
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if (nombre.isNotEmpty() && correo.isNotEmpty() && clave.isNotEmpty()) {
                    viewModel.registro(nombre, correo, clave)
                    Toast.makeText(contexto, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(contexto, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentGreen,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Crear Cuenta",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Ya tienes cuenta? ",
                color = TextoSecundario,
                fontSize = 14.sp
            )
            Text(
                text = "Inicia sesión",
                color = AccentGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                // Hace que el texto sea clicable y regrese a la pantalla anterior al ser presionado
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
