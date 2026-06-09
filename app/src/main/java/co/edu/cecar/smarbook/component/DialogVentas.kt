package co.edu.cecar.smarbook.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import co.edu.cecar.smarbook.model.cliente.ClienteResponse
import co.edu.cecar.smarbook.model.libro.LibroResponse
import co.edu.cecar.smarbook.model.lote.LoteResponse
import co.edu.cecar.smarbook.model.venta.VentaItemRequest
import co.edu.cecar.smarbook.model.venta.VentaRequest

@Composable
fun DialogRegistrarVenta(
    clientes: List<ClienteResponse>,
    libros: List<LibroResponse>,
    lotes: List<LoteResponse>,
    mensajeError: String = "",
    onDismiss: () -> Unit,
    onGuardar: (VentaRequest) -> Unit
) {
    var identificacionCliente by remember { mutableStateOf("") }
    var numeroComprobante by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var buscarLibroText by remember { mutableStateOf("") }
    
    val itemsVenta = remember { mutableStateListOf(VentaItemUI()) }

    val context = LocalContext.current

    val librosFiltrados = libros.filter { 
        it.nombre.contains(buscarLibroText, ignoreCase = true)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.95f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD))
        ) {
            Column {
                Box(
                    modifier = Modifier.fillMaxWidth().background(Color.Red).padding(16.dp)
                ) {
                    Column {
                        Text("Registrar Venta (POS)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Complete los datos de la venta", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    }
                }

                Column(
                    modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()).weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (mensajeError.isNotEmpty()) {
                        Text(
                            text = mensajeError,
                            color = Color.Red,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                    }

                    Text("Datos del Cliente", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    OutlinedTextField(
                        value = identificacionCliente,
                        onValueChange = { identificacionCliente = it },
                        label = { Text("Identificación del cliente") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = numeroComprobante,
                        onValueChange = { numeroComprobante = it },
                        label = { Text("Número de comprobante") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = observaciones,
                        onValueChange = { observaciones = it },
                        label = { Text("Observaciones") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Items de Venta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Button(
                            onClick = { itemsVenta.add(VentaItemUI()) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Agregar")
                        }
                    }

                    OutlinedTextField(
                        value = buscarLibroText,
                        onValueChange = { buscarLibroText = it },
                        placeholder = { Text("Buscar libro...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    itemsVenta.forEachIndexed { index, item ->
                        ItemVentaRow(
                            index = index,
                            item = item,
                            libros = librosFiltrados,
                            lotes = lotes,
                            onRemove = {
                                if (itemsVenta.size > 1) {
                                    itemsVenta.removeAt(index)
                                }
                            },
                            onUpdate = { updatedItem ->
                                itemsVenta[index] = updatedItem
                            }
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    val total = itemsVenta.sumOf { (it.libroSeleccionado?.valorVentaPublico ?: 0.0) * it.cantidad }
                    
                    Box(
                        modifier = Modifier.fillMaxWidth().background(Color.Red, shape = RoundedCornerShape(8.dp)).padding(16.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Total a pagar:", color = Color.White, fontSize = 14.sp)
                                Text("$${String.format("%,.0f", total)}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancelar") }
                        Button(
                            onClick = {
                                if (identificacionCliente.isBlank() || itemsVenta.any { it.libroSeleccionado == null || it.loteSeleccionado == null }) {
                                    Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                onGuardar(
                                    VentaRequest(
                                        identificacionCliente = identificacionCliente,
                                        numeroComprobante = numeroComprobante,
                                        observaciones = observaciones,
                                        items = itemsVenta.map { 
                                            VentaItemRequest(
                                                libroId = it.libroSeleccionado!!.id,
                                                lote = it.loteSeleccionado!!.codigo,
                                                cantidad = it.cantidad
                                            )
                                        }
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Registrar Venta")
                        }
                    }
                }
            }
        }
    }
}

data class VentaItemUI(
    val libroSeleccionado: LibroResponse? = null,
    val loteSeleccionado: LoteResponse? = null,
    val cantidad: Int = 1
)

@Composable
fun ItemVentaRow(
    index: Int,
    item: VentaItemUI,
    libros: List<LibroResponse>,
    lotes: List<LoteResponse>,
    onRemove: () -> Unit,
    onUpdate: (VentaItemUI) -> Unit
) {
    var cantidadText by remember(item.cantidad) { mutableStateOf(if(item.cantidad == 0) "" else item.cantidad.toString()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Item #${index + 1}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                }
            }
            
            Spacer(Modifier.height(8.dp))

            Dropdow(
                items = libros,
                selectedItem = item.libroSeleccionado,
                onItemSelected = { onUpdate(item.copy(libroSeleccionado = it)) },
                label = "Libro",
                placeholder = "Seleccione un libro",
                itemLabel = { it.nombre },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    Dropdow(
                        items = lotes,
                        selectedItem = item.loteSeleccionado,
                        onItemSelected = { onUpdate(item.copy(loteSeleccionado = it)) },
                        label = "Lote",
                        placeholder = "Seleccione el lote",
                        itemLabel = { it.codigo.toString() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = cantidadText,
                    onValueChange = { 
                        cantidadText = it
                        val cant = it.toIntOrNull() ?: 0
                        onUpdate(item.copy(cantidad = cant))
                    },
                    label = { Text("Cant") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(0.5f),
                    singleLine = true
                )
            }
            
            val subtotal = (item.libroSeleccionado?.valorVentaPublico ?: 0.0) * item.cantidad
            Text(
                text = "Subtotal: $${String.format("%,.0f", subtotal)}", 
                color = Color.Red, 
                fontWeight = FontWeight.Bold, 
                modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
            )
        }
    }
}
