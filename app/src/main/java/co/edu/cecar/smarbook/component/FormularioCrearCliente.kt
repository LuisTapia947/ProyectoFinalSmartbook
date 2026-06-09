package co.edu.cecar.smarbook.component

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.edu.cecar.smarbook.model.cliente.ClienteRequest
import java.time.Instant
import java.time.ZoneId

@Composable
fun DialogCrearCliente(
    mensajeError: String,
    onDismiss: () -> Unit,
    onGuardar: (ClienteRequest) -> Unit
) {
    var identificacionState by remember { mutableStateOf("") }
    var nombresState by remember { mutableStateOf("") }
    var correoState by remember { mutableStateOf("") }
    var numeroCelularState by remember { mutableStateOf("") }
    var fechaNacimientoState by remember { mutableStateOf("") }

    var mostrarCalendario by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,

        confirmButton = {
            Button(
                onClick = {
                    if (
                        identificacionState.isBlank() ||
                        nombresState.isBlank() ||
                        correoState.isBlank() ||
                        fechaNacimientoState.isBlank()
                    ) {
                        Toast.makeText(
                            context,
                            "Completa todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    onGuardar(
                        ClienteRequest(
                            identificacion = identificacionState,
                            nombres = nombresState,
                            email = correoState,
                            celular = numeroCelularState,
                            fechaNacimiento = fechaNacimientoState
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Guardar")
            }
        },

        dismissButton = {
            OutlinedButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        },

        title = {
            Text(
                text = "Nuevo Cliente",
                fontWeight = FontWeight.Bold
            )
        },

        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (mensajeError.isNotEmpty()) {
                    Text(
                        text = mensajeError,
                        color = Color.Red
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                OutlinedTextField(
                    value = identificacionState,
                    onValueChange = {
                        identificacionState = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Identificación")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nombresState,
                    onValueChange = {
                        nombresState = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Nombres completos")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = correoState,
                    onValueChange = {
                        correoState = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Email")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = numeroCelularState,
                    onValueChange = {
                        numeroCelularState = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Numero de Celular")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

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

                                        fechaNacimientoState = fecha.toString()
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
                        DatePicker(
                            state = datePickerState
                        )
                    }
                }
            }
        }
    )
}
