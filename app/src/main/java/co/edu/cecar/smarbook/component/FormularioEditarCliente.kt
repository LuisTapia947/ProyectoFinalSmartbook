package co.edu.cecar.smarbook.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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



@Composable
fun DialogEditarCliente(
    cliente: ClienteResponse,
    onDismiss: () -> Unit,
    onGuardar: (ClienteEditarRequest) -> Unit
) {
    var mostrarCalendario by remember {
        mutableStateOf(false)
    }

    var errorValidacion by remember {
        mutableStateOf("")
    }

    val datePickerState = rememberDatePickerState()

    var identificacionState by remember {
        mutableStateOf(cliente.identificacion)
    }

    var nombresState by remember {
        mutableStateOf(cliente.nombres)
    }

    var emailState by remember {
        mutableStateOf(cliente.email)
    }

    var celularState by remember {
        mutableStateOf(cliente.celular)
    }

    var fechaNacimientoState by remember {
        mutableStateOf(cliente.fechaNacimiento)
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {

        Card(
            shape = RoundedCornerShape(16.dp)
        ) {

            Column {

                // HEADER
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

                // FORMULARIO
                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(14.dp)
                ) {

                    if (errorValidacion.isNotEmpty()) {
                        Text(
                            text = errorValidacion,
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    OutlinedTextField(
                        value = identificacionState,
                        onValueChange = {
                            identificacionState = it
                        },
                        label = {
                            Text("Identificación")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = nombresState,
                        onValueChange = {
                            nombresState = it
                        },
                        label = {
                            Text("Nombres completos")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = emailState,
                        onValueChange = {
                            emailState = it
                        },
                        label = {
                            Text("Correo electrónico")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = celularState,
                        onValueChange = {
                            celularState = it
                        },
                        label = {
                            Text("Celular")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = fechaNacimientoState,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Fecha nacimiento")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    mostrarCalendario = true
                                }
                            ) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = "Seleccionar fecha"
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.End
                    ) {

                        OutlinedButton(
                            onClick = {
                                onDismiss()
                            }
                        ) {
                            Text("Cancelar")
                        }

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Button(
                            onClick = {
                                if (identificacionState.isBlank() || nombresState.isBlank() || 
                                    emailState.isBlank() || celularState.isBlank() || 
                                    fechaNacimientoState.isBlank()) {
                                    errorValidacion = "Todos los campos son obligatorios"
                                } else {
                                    errorValidacion = ""
                                    onGuardar(
                                        ClienteEditarRequest(
                                            nombres = nombresState,
                                            email = emailState,
                                            celular = celularState,
                                            fechaNacimiento = fechaNacimientoState
                                        )
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text("Guardar Cliente")
                        }
                    }
                    if (mostrarCalendario) {

                        DatePickerDialog(

                            onDismissRequest = {
                                mostrarCalendario = false
                            },

                            confirmButton = {

                                TextButton(
                                    onClick = {

                                        datePickerState.selectedDateMillis?.let {

                                            val fecha = Instant
                                                .ofEpochMilli(it)
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDate()

                                            fechaNacimientoState =
                                                fecha.toString()
                                        }

                                        mostrarCalendario = false
                                    }
                                ) {
                                    Text("Aceptar")
                                }
                            },

                            dismissButton = {

                                TextButton(
                                    onClick = {
                                        mostrarCalendario = false
                                    }
                                ) {
                                    Text("Cancelar")
                                }
                            }

                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {

                                DatePicker(
                                    state = datePickerState,
                                    modifier = Modifier.scale(0.85f)
                                )
                            }
                        }
                    }
                }
            }
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
    var expanded by remember {
        mutableStateOf(false)
    }

    var errorValidacion by remember {
        mutableStateOf("")
    }

    var nombresState by remember(usuario) {
        mutableStateOf(usuario.nombres)
    }

    var emailState by remember(usuario) {
        mutableStateOf(usuario.email)
    }

    var rolState by remember(usuario) {
        mutableStateOf(usuario.rol)
    }

    var activoState by remember(usuario) {
        mutableStateOf(usuario.activo)
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(
            shape = RoundedCornerShape(16.dp)
        ) {

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
                        Text(
                            text = errorAMostrar,
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    OutlinedTextField(
                        value = usuario.identificacion,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Identificación")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = nombresState,
                        onValueChange = {
                            nombresState = it
                        },
                        label = {
                            Text("Nombres")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = emailState,
                        onValueChange = {
                            emailState = it
                        },
                        label = {
                            Text("Correo")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        val rolLabel = when(rolState) {
                            "Admin" -> "Administrador"
                            "Vendedor" -> "Vendedor"
                            else -> rolState
                        }

                        OutlinedTextField(
                            value = rolLabel,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("Selecciona el rol")
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            DropdownMenuItem(
                                text = {
                                    Text("Administrador")
                                },
                                onClick = {
                                    rolState = "Admin"
                                    expanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Vendedor")
                                },
                                onClick = {
                                    rolState = "Vendedor"
                                    expanded = false
                                }
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Activo"
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Switch(
                            checked = activoState,
                            onCheckedChange = {
                                activoState = it
                            }
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        OutlinedButton(
                            onClick = onDismiss
                        ) {
                            Text("Cancelar")
                        }

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Button(
                            onClick = {
                                if (nombresState.isBlank() || emailState.isBlank() || rolState.isBlank()) {
                                    errorValidacion = "Todos los campos son obligatorios"
                                } else {
                                    errorValidacion = ""
                                    onGuardar(
                                        UsuarioEditarRequest(
                                            nombres = nombresState,
                                            email = emailState,
                                            rol = if (rolState == "Admin") 1 else 2,
                                            activo = activoState
                                        )
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text("Guardar Usuario")
                        }
                    }
                }
            }
        }
    }
}