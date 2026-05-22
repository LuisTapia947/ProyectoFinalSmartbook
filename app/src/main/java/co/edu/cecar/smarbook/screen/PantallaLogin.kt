package co.edu.cecar.smarbook.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.R
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.LoginRepository

import co.edu.cecar.smarbook.model.Login.LoginRequest
import kotlinx.coroutines.launch


@Composable
fun Pantallalogin(
    navegarAPantallaSolicitudRestablecimiento: () -> Unit,
    navegarAPantallaDashboard : () -> Unit
){
    //navegarAPantallaInicio: () -> Unit,


    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }

    var mensajeErrorCredenciales by remember {mutableStateOf("")
    }


    val context = LocalContext.current

    var mensajeCorreo by remember { mutableStateOf("") }
    var mensajeContraseña by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val repository = remember { LoginRepository() }
    val sessionManager = remember { SessionManager(context) }



    var visible by remember { mutableStateOf(false) }

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
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(R.drawable.cdilogo2022),
                    contentDescription = "logo CDI",
                    modifier = Modifier
                        .width(200.dp)
                        .height(70.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(0.dp))
                if (mensajeErrorCredenciales.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFFF3F3),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                Color(0xFFFFCACA),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = "Error",
                            tint = Color.Red
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = mensajeErrorCredenciales,
                            color = Color.Red,
                            fontSize = 15.sp
                        )
                    }


                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = {
                        Text("Correo Electrónico")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Icono correo"
                        )
                    },
                    isError = mensajeCorreo.isNotEmpty(),
                    supportingText = {
                        if (mensajeCorreo.isNotEmpty()){
                            Text(text = mensajeCorreo)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )

                Spacer(modifier = Modifier.height(0.dp))

                OutlinedTextField(
                    value = passwordState,
                    onValueChange = { passwordState = it },
                    label = {
                        Text("Contraseña")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Icono contraseña"
                        )
                    },

                    trailingIcon = {
                        IconButton(
                            onClick = { visible = !visible }
                        ) {
                            Icon(
                                imageVector =
                                    if (visible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                contentDescription = "Ver contraseña"
                            )
                        }
                    },
                    isError = mensajeContraseña.isNotEmpty(),
                    supportingText = {
                        if (mensajeContraseña.isNotEmpty()){
                            Text(text = mensajeContraseña)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation =
                        if (visible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Checkbox(
                            checked = false,
                            onCheckedChange = {}
                        )

                        Text("Recordarme")
                    }

                    Spacer(modifier = Modifier.padding(24.dp))

                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        fontSize = 12.sp,
                        color = Color(0xFFDF0A0B),
                        modifier = Modifier.clickable {
                            navegarAPantallaSolicitudRestablecimiento()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        mensajeCorreo = ""
                        mensajeContraseña = ""
                        mensajeErrorCredenciales = ""

                        if (emailState.isBlank()){
                            mensajeCorreo="Ingresar correo"
                        }
                        if (passwordState.isBlank()){
                            mensajeContraseña="Ingresar Contraseña"
                        }

                        if (
                            mensajeCorreo.isEmpty() &&
                            mensajeContraseña.isEmpty()
                        ) {
                            val loginRequest = LoginRequest(
                                email = emailState,
                                password = passwordState
                            )
                            scope.launch {
                                repository.login(loginRequest)
                                    .onSuccess { response ->

                                        sessionManager.saveSession(
                                            token = response.token,
                                            rol = response.usuario.rol,
                                            nombre = response.usuario.nombres
                                        )

                                        Toast.makeText(
                                            context,
                                            "Bienvenido ${response.usuario.nombres}",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navegarAPantallaDashboard()
                                    }
                                    .onFailure {
                                        mensajeErrorCredenciales = "Credenciales inválidas."
                                    }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Ingresar")
                }
            }
        }
    }
}
