package co.edu.cecar.smarbook.screen

import android.R.attr.text
import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.R

@Composable
fun PantallaNuevaContrasena(
    navegarAPantallaSolicitudRestablecimiento: () -> Unit,
    NavegarPantallaLogin:()-> Unit

){

    var  codigoState by remember() { mutableStateOf("") }
    var nuevaContrasenaState by remember { mutableStateOf("") }
    var confirmarContrsenaState by remember { mutableStateOf("") }

    var errorCodigo by remember { mutableStateOf("") }
    var errorNuevaContrasena by remember { mutableStateOf("") }
    var errorConfirmar by remember { mutableStateOf("") }

    BackHandler {
        navegarAPantallaSolicitudRestablecimiento()
    }

    val context = LocalContext.current

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
                .padding(top =40 .dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navegarAPantallaSolicitudRestablecimiento()
                    }
                ) {

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
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
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

                Text("Establecer tu nueva contrasena")

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = codigoState,
                    onValueChange = { codigoState = it },
                    label = {
                        Text("Codigo de verificacion")
                    },
                    isError = errorCodigo.isNotEmpty(),
                    supportingText = {
                        if (errorCodigo.isNotEmpty()) {
                            Text(text = errorCodigo)
                        }
                    }



                )

                Spacer(modifier = Modifier.height(0.dp))

                OutlinedTextField(
                    value = nuevaContrasenaState,
                    onValueChange = { nuevaContrasenaState = it },
                    label = {
                        Text("Nueva Contraseña")
                    },
                       isError = errorNuevaContrasena.isNotEmpty(),
                       supportingText = {
                        if (errorNuevaContrasena.isNotEmpty()) {
                            Text(text = errorNuevaContrasena)
                        }
                    }


                )

                Spacer(modifier = Modifier.height(0.dp))

                OutlinedTextField(
                    value = confirmarContrsenaState,
                    onValueChange = { confirmarContrsenaState = it },
                    label = {
                        Text("Confirmar Contraseña")
                        },
                        isError = errorConfirmar.isNotEmpty(),
                        supportingText = {
                        if (errorConfirmar.isNotEmpty()) {
                            Text(text =errorConfirmar)
                        }
                    }



                )

                Spacer(modifier = Modifier.height(0.dp))

                Row() {
                    Button(
                        onClick = {navegarAPantallaSolicitudRestablecimiento()}
                    ) {
                        Text("Volver")
                    }
                    Spacer(modifier = Modifier.padding(12.dp))
                    Button(
                        onClick = {
                            errorCodigo = ""
                            errorNuevaContrasena = ""
                            errorConfirmar = ""

                            if (codigoState.isBlank()) {
                                errorCodigo = "Debe ingresar el código"

                            }

                            if (nuevaContrasenaState.isBlank()) {
                                errorNuevaContrasena = "Debe ingresar una contraseña"

                            }

                            if (confirmarContrsenaState.isBlank()) {
                                errorConfirmar = "Debe confirmar la contraseña"

                            }

                            if (
                                nuevaContrasenaState.isNotBlank() &&
                                confirmarContrsenaState.isNotBlank() &&
                                nuevaContrasenaState != confirmarContrsenaState
                            ) {
                                errorConfirmar = "Las contraseñas no coinciden"

                            }

                            if (
                                errorCodigo.isEmpty() &&
                                errorNuevaContrasena.isEmpty() &&
                                errorConfirmar.isEmpty()
                            ) {
                                Toast.makeText(
                                    context,
                                    "Contraseña restablecida correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()

                                NavegarPantallaLogin()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().weight(2f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )

                    ) {
                        Text("Restablecer Contraseña",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }


                }



                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "← Volver al inicio de sesión",
                    fontSize = 12.sp,
                    color = Color(0xFFDF0A0B),
                    modifier = Modifier.clickable {
                        NavegarPantallaLogin()
                    }
                )
            }
        }
    }

}

