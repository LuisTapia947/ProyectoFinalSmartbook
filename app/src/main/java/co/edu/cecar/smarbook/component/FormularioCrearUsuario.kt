package co.edu.cecar.smarbook.component

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import co.edu.cecar.smarbook.model.usuario.UsuarioRequest

@Composable
fun DialogCrearUsuario(
    mensajeError: String,
    onDismiss: () -> Unit,
    onGuardar: (UsuarioRequest) -> Unit
) {
    var identificacionState by remember { mutableStateOf("") }
    var nombresState by remember { mutableStateOf("") }
    var correoState by remember { mutableStateOf("") }
    var contrasenaState by remember { mutableStateOf("") }
    
    var rolSeleccionadoLabel by remember { mutableStateOf("Selecciona un rol") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (identificacionState.isBlank() || nombresState.isBlank() ||
                        correoState.isBlank() || contrasenaState.isBlank() || 
                        rolSeleccionadoLabel == "Selecciona un rol") {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (!correoState.endsWith("@yopmail.com")) {
                        Toast.makeText(context, "Solo se permiten correos @yopmail.com", Toast.LENGTH_SHORT).show()
                        return@Button
                    }


                    val rolIdFinal = if (rolSeleccionadoLabel == "Administrador") 1 else 2

                    onGuardar(
                        UsuarioRequest(
                            identificacion = identificacionState,
                            nombres = nombresState,
                            email = correoState,
                            password = contrasenaState,
                            rol = rolIdFinal
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
        },
        title = {
            Text(text = "Nuevo Usuario", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (mensajeError.isNotEmpty()) {
                    Text(text = mensajeError, color = Color.Red)
                }

                OutlinedTextField(
                    value = identificacionState,
                    onValueChange = { identificacionState = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Identificación") }
                )

                OutlinedTextField(
                    value = nombresState,
                    onValueChange = { nombresState = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombres completos") }
                )

                OutlinedTextField(
                    value = correoState,
                    onValueChange = { correoState = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") }
                )

                OutlinedTextField(
                    value = contrasenaState,
                    onValueChange = { contrasenaState = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation()
                )

                Box {
                    OutlinedTextField(
                        value = rolSeleccionadoLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de rol") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Administrador") },
                            onClick = {
                                rolSeleccionadoLabel = "Administrador"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Vendedor") },
                            onClick = {
                                rolSeleccionadoLabel = "Vendedor"
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    )
}
