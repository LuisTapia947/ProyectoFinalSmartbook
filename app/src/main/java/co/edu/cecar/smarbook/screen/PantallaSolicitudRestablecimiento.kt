package co.edu.cecar.smarbook.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.R
import co.edu.cecar.smarbook.data.repository.LoginRepository
import co.edu.cecar.smarbook.model.Login.RestablecimientoRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PantallaSolicitudRestablecimiento(
    NavegarPantallaLogin: () -> Unit,
    NavegarPantallaNuevaContrasena: () -> Unit
) {
    var correoState by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val repository = remember { LoginRepository() }

    LaunchedEffect(mensajeExito) {
        if (mensajeExito.isNotEmpty()) {
            delay(3000)
            NavegarPantallaNuevaContrasena()
        }
    }

    BackHandler {
        NavegarPantallaLogin()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.slide),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.85f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { NavegarPantallaLogin() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Volver",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.cdilogo2022),
                    contentDescription = "Logo CDI",
                    modifier = Modifier
                        .width(200.dp)
                        .height(70.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Restablecer Contraseña",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (mensajeExito.isNotEmpty()) {
                    Text(
                        text = mensajeExito,
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (mensajeError.isNotEmpty()) {
                    Text(
                        text = mensajeError,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = correoState,
                    onValueChange = { 
                        correoState = it
                        mensajeError = ""
                    },
                    label = { Text("Correo Electrónico") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Icono correo"
                        )
                    },
                    isError = mensajeError.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !cargando,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (correoState.isBlank()) {
                            mensajeError = "Por favor, ingresa tu correo"
                            return@Button
                        }
                        
                        cargando = true
                        mensajeError = ""
                        mensajeExito = ""
                        
                        scope.launch {
                            repository.solicitarRestablecimiento(RestablecimientoRequest(correoState))
                                .onSuccess { response ->
                                    mensajeExito = response.mensaje
                                    cargando = false
                                }
                                .onFailure { error ->
                                    mensajeError = error.message ?: "Error al procesar la solicitud"
                                    cargando = false
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    if (cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Solicitar Código")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                
                Text(
                    text = "← Volver al inicio de sesión",
                    fontSize = 12.sp,
                    color = Color(0xFFDF0A0B),
                    modifier = Modifier.clickable(enabled = !cargando) {
                        NavegarPantallaLogin()
                    }
                )
            }
        }
    }
}
