package co.edu.cecar.smarbook.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.model.libro.LibroEditarRequest
import co.edu.cecar.smarbook.model.libro.LibroRequest
import co.edu.cecar.smarbook.model.libro.LibroResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogCrearLibro(

    onDismiss: () -> Unit,

    onGuardar: (LibroRequest) -> Unit
) {

    var nombre by remember { mutableStateOf("") }
    var nivel by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var edicion by remember { mutableStateOf("") }
    var unidades by remember { mutableStateOf("") }
    var lote by remember { mutableStateOf("") }
    var valorCompra by remember { mutableStateOf("") }
    var valorVentaPublico by remember { mutableStateOf("") }

    var expanded by remember {
        mutableStateOf(false)
    }

    var errorNombre by remember {
        mutableStateOf("")
    }

    var errorNivel by remember {
        mutableStateOf("")
    }

    var errorTipo by remember {
        mutableStateOf("")
    }

    var errorEdicion by remember {
        mutableStateOf("")
    }

    var errorLote by remember {
        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        confirmButton = {

            Button(

                onClick = {

                    errorNombre = ""
                    errorNivel = ""
                    errorTipo = ""
                    errorEdicion = ""
                    errorLote = ""

                    var valido = true

                    if (nombre.isBlank()) {
                        errorNombre = "Debe ingresar el nombre"
                        valido = false
                    }

                    if (nivel.isBlank()) {
                        errorNivel = "Debe ingresar el nivel"
                        valido = false
                    }

                    if (tipo.isBlank()) {
                        errorTipo = "Debe seleccionar un tipo"
                        valido = false
                    }

                    if (edicion.isBlank()) {
                        errorEdicion = "Debe ingresar la edición"
                        valido = false
                    }

                    if ((lote.toIntOrNull() ?: 0) <= 0) {
                        errorLote = "Debe ingresar un lote válido"
                        valido = false
                    }

                    if ((unidades.toIntOrNull() ?: 0) < 0) {
                        valido = false
                    }

                    if ((valorCompra.toDoubleOrNull() ?: 0.0) < 0) {
                        valido = false
                    }

                    if ((valorVentaPublico.toDoubleOrNull() ?: 0.0) < 0) {
                        valido = false
                    }

                    if (!valido) return@Button

                    val tipoNumero = when (tipo) {

                        "Student's Book" -> 1

                        "Workbook" -> 2


                        else -> 1
                    }

                    onGuardar(

                        LibroRequest(

                            nombre = nombre,

                            nivel = nivel,

                            tipo = tipoNumero,

                            edicion = edicion,

                            unidades = unidades.toIntOrNull() ?: 0,

                            lote = lote.toIntOrNull() ?: 0,

                            valorCompra =
                                valorCompra.toDoubleOrNull() ?: 0.0,

                            valorVentaPublico =
                                valorVentaPublico.toDoubleOrNull() ?: 0.0
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
                text = "Nuevo Libro",
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

                OutlinedTextField(

                    value = nombre,

                    onValueChange = {

                        nombre = it
                        errorNombre = ""
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Nombre")
                    },

                    isError =
                        errorNombre.isNotEmpty()
                )

                if (errorNombre.isNotEmpty()) {

                    Text(
                        text = errorNombre,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = nivel,

                    onValueChange = {

                        nivel = it
                        errorNivel = ""
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Nivel")
                    },

                    isError =
                        errorNivel.isNotEmpty()
                )

                if (errorNivel.isNotEmpty()) {

                    Text(
                        text = errorNivel,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                ExposedDropdownMenuBox(

                    expanded = expanded,

                    onExpandedChange = {

                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(

                        value = tipo,

                        onValueChange = {},

                        readOnly = true,

                        label = {
                            Text("Tipo")
                        },

                        placeholder = {
                            Text("Seleccione tipo...")
                        },

                        trailingIcon = {

                            ExposedDropdownMenuDefaults
                                .TrailingIcon(
                                    expanded = expanded
                                )
                        },

                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),

                        isError =
                            errorTipo.isNotEmpty()
                    )

                    ExposedDropdownMenu(

                        expanded = expanded,

                        onDismissRequest = {

                            expanded = false
                        }
                    ) {

                        DropdownMenuItem(

                            text = {

                                Text(
                                    "Student's Book"
                                )
                            },

                            onClick = {

                                tipo =
                                    "Student's Book"

                                errorTipo = ""

                                expanded = false
                            }
                        )

                        DropdownMenuItem(

                            text = {

                                Text(
                                    "Workbook"
                                )
                            },

                            onClick = {

                                tipo = "Workbook"

                                errorTipo = ""

                                expanded = false
                            }
                        )


                    }
                }

                if (errorTipo.isNotEmpty()) {

                    Text(
                        text = errorTipo,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = edicion,

                    onValueChange = {

                        edicion = it
                        errorEdicion = ""
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Edición")
                    },

                    isError =
                        errorEdicion.isNotEmpty()
                )

                if (errorEdicion.isNotEmpty()) {

                    Text(
                        text = errorEdicion,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = unidades,

                    onValueChange = {
                        unidades = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Unidades")
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType =
                            KeyboardType.Number
                    )
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = lote,

                    onValueChange = {

                        lote = it
                        errorLote = ""
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Lote")
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType =
                            KeyboardType.Number
                    ),

                    isError =
                        errorLote.isNotEmpty()
                )

                if (errorLote.isNotEmpty()) {

                    Text(
                        text = errorLote,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = valorCompra,

                    onValueChange = {
                        valorCompra = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Valor Compra")
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    )
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = valorVentaPublico,

                    onValueChange = {
                        valorVentaPublico = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Valor Venta Público")
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    )
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogEditarLibro(

    libro: LibroResponse,

    onDismiss: () -> Unit,

    onGuardar: (LibroEditarRequest) -> Unit
) {

    var nombre by remember {
        mutableStateOf(libro.nombre)
    }

    var nivel by remember {
        mutableStateOf(libro.nivel)
    }

    var tipo by remember {
        mutableStateOf(
            when (libro.tipo) {
                "StudentsBook" -> "Student's Book"
                "Workbook" -> "Workbook"
                else -> libro.tipo
            }
        )
    }

    var edicion by remember {
        mutableStateOf(libro.edicion)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var errorNombre by remember {
        mutableStateOf("")
    }

    var errorNivel by remember {
        mutableStateOf("")
    }

    var errorTipo by remember {
        mutableStateOf("")
    }

    var errorEdicion by remember {
        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        confirmButton = {

            Button(

                onClick = {

                    errorNombre = ""
                    errorNivel = ""
                    errorTipo = ""
                    errorEdicion = ""

                    var valido = true

                    if (nombre.isBlank()) {

                        errorNombre =
                            "Debe ingresar el nombre"

                        valido = false
                    }

                    if (nivel.isBlank()) {

                        errorNivel =
                            "Debe ingresar el nivel"

                        valido = false
                    }

                    if (tipo.isBlank()) {

                        errorTipo =
                            "Debe seleccionar un tipo"

                        valido = false
                    }

                    if (edicion.isBlank()) {

                        errorEdicion =
                            "Debe ingresar la edición"

                        valido = false
                    }

                    if (!valido) {
                        return@Button
                    }

                    val tipoNumero = when (tipo) {

                        "Student's Book" -> 1

                        "Workbook" -> 2


                        else -> 1
                    }

                    onGuardar(

                        LibroEditarRequest(

                            nombre = nombre,

                            nivel = nivel,

                            tipo = tipoNumero,

                            edicion = edicion
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
                text = "Editar Libro",
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

                OutlinedTextField(

                    value = nombre,

                    onValueChange = {
                        nombre = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Nombre")
                    },

                    isError = errorNombre.isNotEmpty()
                )

                if (errorNombre.isNotEmpty()) {

                    Text(
                        text = errorNombre,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = nivel,

                    onValueChange = {
                        nivel = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Nivel")
                    },

                    isError = errorNivel.isNotEmpty()
                )

                if (errorNivel.isNotEmpty()) {

                    Text(
                        text = errorNivel,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                ExposedDropdownMenuBox(

                    expanded = expanded,

                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(

                        value = tipo,

                        onValueChange = {},

                        readOnly = true,

                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),

                        label = {
                            Text("Tipo")
                        },

                        trailingIcon = {

                            ExposedDropdownMenuDefaults
                                .TrailingIcon(
                                    expanded = expanded
                                )
                        },

                        isError = errorTipo.isNotEmpty()
                    )

                    ExposedDropdownMenu(

                        expanded = expanded,

                        onDismissRequest = {
                            expanded = false
                        }
                    ) {

                        DropdownMenuItem(

                            text = {
                                Text("Student's Book")
                            },

                            onClick = {

                                tipo = "Student's Book"

                                expanded = false
                            }
                        )

                        DropdownMenuItem(

                            text = {
                                Text("Workbook")
                            },

                            onClick = {

                                tipo = "Workbook"

                                expanded = false
                            }
                        )


                    }
                }

                if (errorTipo.isNotEmpty()) {

                    Text(
                        text = errorTipo,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = edicion,

                    onValueChange = {
                        edicion = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Edición")
                    },

                    isError = errorEdicion.isNotEmpty()
                )

                if (errorEdicion.isNotEmpty()) {

                    Text(
                        text = errorEdicion,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        }
    )
}