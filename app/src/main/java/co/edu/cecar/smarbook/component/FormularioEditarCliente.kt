package co.edu.cecar.smarbook.component

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.Instant
import java.time.ZoneId



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogEditarCliente(
    cliente: ClienteResponse,
    onDismiss: () -> Unit,
    onGuardar: (ClienteEditarRequest) -> Unit
) {
    var mostrarCalendario by remember {
        mutableStateOf(false)
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
                                onGuardar(
                                    ClienteEditarRequest(
                                        nombres = nombresState,
                                        email = emailState,
                                        celular = celularState,
                                        fechaNacimiento = fechaNacimientoState
                                    )
                                )
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