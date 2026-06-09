package co.edu.cecar.smarbook.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import co.edu.cecar.smarbook.model.cliente.ClienteEditarRequest
import co.edu.cecar.smarbook.model.cliente.ClienteResponse
import co.edu.cecar.smarbook.model.usuario.UsuarioEditarRequest
import co.edu.cecar.smarbook.model.usuario.UsuarioResponse
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogEditarCliente(
    cliente: ClienteResponse,
    onDismiss: () -> Unit,
    onGuardar: (ClienteEditarRequest) -> Unit
) {
    var mostrarCalendario by remember { mutableStateOf(false) }
    var errorValidacion by remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()

    var identificacionState by remember { mutableStateOf(cliente.identificacion) }
    var nombresState by remember { mutableStateOf(cliente.nombres) }
    var emailState by remember { mutableStateOf(cliente.email) }
    var celularState by remember { mutableStateOf(cliente.celular) }
    var fechaNacimientoState by remember { mutableStateOf(cliente.fechaNacimiento) }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Editar Cliente",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (errorValidacion.isNotEmpty()) {
                        Text(text = errorValidacion, color = Color.Red, fontSize = 14.sp)
                    }

                    OutlinedTextField(
                        value = identificacionState,
                        onValueChange = { identificacionState = it },
                        label = { Text("Identificación") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = nombresState,
                        onValueChange = { nombresState = it },
                        label = { Text("Nombres completos") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = emailState,
                        onValueChange = { emailState = it },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = celularState,
                        onValueChange = { celularState = it },
                        label = { Text("Celular") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = fechaNacimientoState,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fecha nacimiento") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { mostrarCalendario = true }) {
                                Icon(Icons.Default.DateRange, contentDescription = null)
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                if (identificacionState.isBlank() || nombresState.isBlank() || 
                                    emailState.isBlank() || celularState.isBlank()) {
                                    errorValidacion = "Campos obligatorios vacíos"
                                } else {
                                    onGuardar(ClienteEditarRequest(nombresState, emailState, celularState, fechaNacimientoState))
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Guardar Cliente")
                        }
                    }
                }
            }
        }
    }

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        fechaNacimientoState = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault()).toLocalDate().toString()
                    }
                    mostrarCalendario = false
                }) { Text("Aceptar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogEditarUsuario(
    usuario: UsuarioResponse,
    mensajeError: String = "",
    onDismiss: () -> Unit,
    onGuardar: (UsuarioEditarRequest) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var errorValidacion by remember { mutableStateOf("") }

    var nombresState by remember(usuario) { mutableStateOf(usuario.nombres) }
    var emailState by remember(usuario) { mutableStateOf(usuario.email) }
    var activoState by remember(usuario) { mutableStateOf(usuario.activo) }

    // Usar etiquetas legibles directamente para que sea IMPOSIBLE invertirlas
    var rolSeleccionadoLabel by remember(usuario) {
        val label = when(usuario.rol) {
            "Admin", "1" -> "Administrador"
            "Vendedor", "0", "2" -> "Vendedor"
            else -> "Vendedor"
        }
        mutableStateOf(label)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Editar Usuario",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val errorAMostrar = if (errorValidacion.isNotEmpty()) errorValidacion else mensajeError
                    if (errorAMostrar.isNotEmpty()) {
                        Text(text = errorAMostrar, color = Color.Red, fontSize = 14.sp)
                    }

                    OutlinedTextField(
                        value = usuario.identificacion,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Identificación") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = nombresState,
                        onValueChange = { nombresState = it },
                        label = { Text("Nombres") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = emailState,
                        onValueChange = { emailState = it },
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Selector de Rol con mapeo manual e instantáneo
                    Box {
                        OutlinedTextField(
                            value = rolSeleccionadoLabel,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Selecciona el rol") },
                            trailingIcon = { 
                                IconButton(onClick = { expanded = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
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

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Activo")
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(checked = activoState, onCheckedChange = { activoState = it })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                if (nombresState.isBlank() || emailState.isBlank()) {
                                    errorValidacion = "Todos los campos son obligatorios"
                                } else {
                                    // Mapeo SEGURO: Si dice Admin en pantalla, envía 2. Si dice Vendedor, envía 1.
                                    val rolIdFinal = if (rolSeleccionadoLabel == "Administrador") 1 else 2
                                    
                                    onGuardar(
                                        UsuarioEditarRequest(
                                            nombres = nombresState,
                                            email = emailState,
                                            rol = rolIdFinal,
                                            activo = activoState
                                        )
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Guardar Usuario")
                        }
                    }
                }
            }
        }
    }
}
