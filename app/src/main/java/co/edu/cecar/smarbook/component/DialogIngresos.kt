package co.edu.cecar.smarbook.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import co.edu.cecar.smarbook.model.ingresos.IngresoRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.edu.cecar.smarbook.model.libro.LibroResponse
import co.edu.cecar.smarbook.model.lote.LoteResponse

@Composable
fun DialogCrearIngreso(
    libros: List<LibroResponse>,
    lotes: List<LoteResponse>,
    onDismiss: () -> Unit,
    onGuardar: (IngresoRequest) -> Unit
) {
    var libroSeleccionado by remember { mutableStateOf<LibroResponse?>(null) }
    var loteSeleccionado by remember { mutableStateOf<LoteResponse?>(null) }
    var unidades by remember { mutableStateOf("") }
    var valorCompra by remember { mutableStateOf("") }
    var valorVentaPublico by remember { mutableStateOf("") }
    var expandirLibros by remember { mutableStateOf(false) }
    var expandirLotes by remember { mutableStateOf(false) }
    var errorValidacion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (libroSeleccionado == null || loteSeleccionado == null || 
                        unidades.isBlank() || valorCompra.isBlank() || valorVentaPublico.isBlank()) {
                        errorValidacion = "Todos los campos son obligatorios"
                    } else {
                        val unidadesInt = unidades.toIntOrNull()
                        val compraDouble = valorCompra.toDoubleOrNull()
                        val ventaDouble = valorVentaPublico.toDoubleOrNull()

                        if (unidadesInt == null || compraDouble == null || ventaDouble == null) {
                            errorValidacion = "Ingrese valores numéricos válidos"
                        } else {
                            errorValidacion = ""
                            onGuardar(
                                IngresoRequest(
                                    libroId = libroSeleccionado!!.id,
                                    unidades = unidadesInt,
                                    lote = loteSeleccionado!!.codigo,
                                    valorCompra = compraDouble,
                                    valorVentaPublico = ventaDouble
                                )
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = {
            Text(text = "Nuevo Ingreso", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (errorValidacion.isNotEmpty()) {
                    Text(text = errorValidacion, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Box {
                    OutlinedTextField(
                        value = libroSeleccionado?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Libro") },
                        trailingIcon = {
                            IconButton(onClick = { expandirLibros = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expandirLibros,
                        onDismissRequest = { expandirLibros = false }
                    ) {
                        libros.forEach { libro ->
                            DropdownMenuItem(
                                text = { Text(libro.nombre) },
                                onClick = {
                                    libroSeleccionado = libro
                                    expandirLibros = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box {
                    OutlinedTextField(
                        value = loteSeleccionado?.codigo?.toString() ?: "",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Lote") },
                        trailingIcon = {
                            IconButton(onClick = { expandirLotes = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expandirLotes,
                        onDismissRequest = { expandirLotes = false }
                    ) {
                        lotes.forEach { lote ->
                            DropdownMenuItem(
                                text = { Text(lote.codigo.toString()) },
                                onClick = {
                                    loteSeleccionado = lote
                                    expandirLotes = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = unidades,
                    onValueChange = { unidades = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Unidades") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = valorCompra,
                    onValueChange = { valorCompra = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Valor Compra") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = valorVentaPublico,
                    onValueChange = { valorVentaPublico = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Valor Venta Público") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        }
    )
}
