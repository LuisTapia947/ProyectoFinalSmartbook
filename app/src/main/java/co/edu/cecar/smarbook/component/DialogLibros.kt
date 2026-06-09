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
import co.edu.cecar.smarbook.model.libro.LibroEditarRequest
import co.edu.cecar.smarbook.model.libro.LibroRequest
import co.edu.cecar.smarbook.model.libro.LibroResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogCrearLibro(

    onDismiss: () -> Unit,

    onGuardar: (LibroRequest) -> Unit
) {

    var nombre by remember {
        mutableStateOf("")
    }

    var nivel by remember {
        mutableStateOf("")
    }

    var tipo by remember {
        mutableStateOf("")
    }

    var edicion by remember {
        mutableStateOf("")
    }

    var unidades by remember {
        mutableStateOf("")
    }

    var lote by remember {
        mutableStateOf("")
    }

    var valorCompra by remember {
        mutableStateOf("")
    }

    var valorVentaPublico by remember {
        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        confirmButton = {

            Button(

                onClick = {

                    val tipoNumero = when (tipo) {

                        "StudentsBook" -> 1

                        "Workbook" -> 2

                        "TeacherBook" -> 3

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

            Column( modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())) {

                OutlinedTextField(

                    value = nombre,

                    onValueChange = {
                        nombre = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Nombre")
                    }
                )

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
                    }
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = tipo,

                    onValueChange = {
                        tipo = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Tipo")
                    },

                    placeholder = {
                        Text("StudentsBook")
                    }
                )

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
                    }
                )

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
                        keyboardType = KeyboardType.Number
                    )
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = lote,

                    onValueChange = {
                        lote = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Lote")
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

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
                        keyboardType = KeyboardType.Decimal
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
                        keyboardType = KeyboardType.Decimal
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
        mutableStateOf(libro.tipo)
    }

    var edicion by remember {
        mutableStateOf(libro.edicion)
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        confirmButton = {

            Button(

                onClick = {

                    val tipoNumero = when (tipo) {

                        "StudentsBook" -> 1

                        "Workbook" -> 2

                        "TeacherBook" -> 3

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

            Column( modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())) {

                OutlinedTextField(

                    value = nombre,

                    onValueChange = {
                        nombre = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Nombre")
                    }
                )

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
                    }
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = tipo,

                    onValueChange = {
                        tipo = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Tipo")
                    }
                )

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
                    }
                )
            }
        }
    )
}