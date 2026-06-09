package co.edu.cecar.smarbook.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.component.DialogRegistrarVenta
import co.edu.cecar.smarbook.component.Dropdow
import co.edu.cecar.smarbook.component.ItemDato
import co.edu.cecar.smarbook.component.ItemListado
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.ClienteRepository
import co.edu.cecar.smarbook.data.repository.LibroRepository
import co.edu.cecar.smarbook.data.repository.LoteRepository
import co.edu.cecar.smarbook.data.repository.VentaRepository
import co.edu.cecar.smarbook.model.cliente.ClienteResponse
import co.edu.cecar.smarbook.model.libro.LibroResponse
import co.edu.cecar.smarbook.model.lote.LoteResponse
import co.edu.cecar.smarbook.model.venta.VentaResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaVentas() {
    var mostrarCrearVentaState by remember { mutableStateOf(false) }
    var mostrarDetalleVentaState by remember { mutableStateOf(false) }
    var ventaSeleccionada by remember { mutableStateOf<VentaResponse?>(null) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    val ventaRepo = remember { VentaRepository() }
    val clienteRepo = remember { ClienteRepository() }
    val libroRepo = remember { LibroRepository() }
    val loteRepo = remember { LoteRepository() }

    val scope = rememberCoroutineScope()

    var buscar by remember { mutableStateOf("") }
    var fechaDesde by remember { mutableStateOf("") }
    var fechaHasta by remember { mutableStateOf("") }
    var libroFiltro by remember { mutableStateOf<LibroResponse?>(null) }

    var mostrarCalendarioDesde by remember { mutableStateOf(false) }
    var mostrarCalendarioHasta by remember { mutableStateOf(false) }
    val datePickerDesdeState = rememberDatePickerState()
    val datePickerHastaState = rememberDatePickerState()

    var listaVentas by remember { mutableStateOf<List<VentaResponse>>(emptyList()) }
    var listaClientes by remember { mutableStateOf<List<ClienteResponse>>(emptyList()) }
    var listaLibros by remember { mutableStateOf<List<LibroResponse>>(emptyList()) }
    var listaLotes by remember { mutableStateOf<List<LoteResponse>>(emptyList()) }
    var mensajeError by remember { mutableStateOf("") }

    fun cargarVentas() {
        scope.launch {
            val token = sessionManager.getToken().first()
            if (token != null) {
                ventaRepo.listarVentas(token, libroFiltro?.id).onSuccess { 
                    listaVentas = it 
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val token = sessionManager.getToken().first()
        if (token != null) {
            launch { cargarVentas() }
            launch { clienteRepo.listarClientes(token).onSuccess { listaClientes = it } }
            launch { libroRepo.listarLibros(token).onSuccess { listaLibros = it } }
            launch { loteRepo.listarLotes(token).onSuccess { listaLotes = it } }
        }
    }

    LaunchedEffect(libroFiltro) {
        cargarVentas()
    }

    val ventasFiltradas = listaVentas.filter { venta ->
        val coincideBusqueda = venta.numeroRecibo.contains(buscar, ignoreCase = true) || 
                              venta.clienteNombre.contains(buscar, ignoreCase = true)
        val fechaVentaStr = venta.fecha.split("T")[0]
        val coincideDesde = if (fechaDesde.isEmpty()) true else fechaVentaStr >= fechaDesde
        val coincideHasta = if (fechaHasta.isEmpty()) true else fechaVentaStr <= fechaHasta
        coincideBusqueda && coincideDesde && coincideHasta
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    mensajeError = ""
                    mostrarCrearVentaState = true 
                },
                containerColor = Color.Red,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Venta")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Terminal de Ventas (POS)", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "Sistema punto de venta - SmartBook", fontSize = 12.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = fechaDesde,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("DESDE", fontSize = 10.sp) },
                            placeholder = { Text("dd/mm/aaaa") },
                            modifier = Modifier.weight(1f),
                            trailingIcon = {
                                IconButton(onClick = { mostrarCalendarioDesde = true }) {
                                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
                                }
                            }
                        )
                        OutlinedTextField(
                            value = fechaHasta,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("HASTA", fontSize = 10.sp) },
                            placeholder = { Text("dd/mm/aaaa") },
                            modifier = Modifier.weight(1f),
                            trailingIcon = {
                                IconButton(onClick = { mostrarCalendarioHasta = true }) {
                                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
                                }
                            }
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.weight(1f)) {
                            Dropdow(
                                items = listaLibros,
                                selectedItem = libroFiltro,
                                onItemSelected = { libroFiltro = it },
                                label = "LIBRO",
                                placeholder = "Todos los libros",
                                itemLabel = { it.nombre },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        IconButton(
                            onClick = {
                                fechaDesde = ""
                                fechaHasta = ""
                                libroFiltro = null
                                buscar = ""
                            },
                            modifier = Modifier.background(Color.Gray.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Limpiar", tint = Color.DarkGray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Historial de Ventas", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            
            Spacer(modifier = Modifier.height(12.dp))

            if (ventasFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron ventas", color = Color.Gray)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ventasFiltradas.forEach { venta ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            ItemListado(
                                titulo = venta.numeroRecibo,
                                datos = listOf(
                                    ItemDato(Icons.Default.Person, "Cliente: ${venta.clienteNombre}"),
                                    ItemDato(Icons.Default.Payments, "Total: $${String.format("%,.0f", venta.total)}"),
                                    ItemDato(Icons.Default.CalendarToday, "Fecha: ${venta.fecha.split("T")[0]}")
                                ),
                                textoAccion = "Ver",
                                onEditar = {
                                    ventaSeleccionada = venta
                                    mostrarDetalleVentaState = true
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp)) // Espacio para el FAB
        }
    }

    if (mostrarCalendarioDesde) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendarioDesde = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerDesdeState.selectedDateMillis?.let {
                        fechaDesde = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().toString()
                    }
                    mostrarCalendarioDesde = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { mostrarCalendarioDesde = false }) { Text("Cancelar") } }
        ) { DatePicker(state = datePickerDesdeState) }
    }

    if (mostrarCalendarioHasta) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendarioHasta = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerHastaState.selectedDateMillis?.let {
                        fechaHasta = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().toString()
                    }
                    mostrarCalendarioHasta = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { mostrarCalendarioHasta = false }) { Text("Cancelar") } }
        ) { DatePicker(state = datePickerHastaState) }
    }

    if (mostrarCrearVentaState) {
        DialogRegistrarVenta(
            clientes = listaClientes,
            libros = listaLibros,
            lotes = listaLotes,
            mensajeError = mensajeError,
            onDismiss = { 
                mostrarCrearVentaState = false 
                mensajeError = ""
            },
            onGuardar = { request ->
                scope.launch {
                    val token = sessionManager.getToken().first()
                    if (token != null) {
                        ventaRepo.registrarVenta(token, request)
                            .onSuccess {
                                Toast.makeText(context, "Venta registrada con éxito", Toast.LENGTH_LONG).show()
                                cargarVentas()
                                mostrarCrearVentaState = false
                                mensajeError = ""
                            }
                            .onFailure { error ->
                                mensajeError = error.message ?: "Error al registrar venta"
                            }
                    }
                }
            }
        )
    }

    if (mostrarDetalleVentaState && ventaSeleccionada != null) {
        AlertDialog(
            onDismissRequest = { mostrarDetalleVentaState = false },
            confirmButton = {
                Button(onClick = { mostrarDetalleVentaState = false }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Cerrar")
                }
            },
            title = { Text("Detalle de Venta", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Número de Recibo:", fontSize = 12.sp)
                            Text(ventaSeleccionada!!.numeroRecibo, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Cliente:", fontSize = 12.sp)
                            Text(ventaSeleccionada!!.clienteNombre, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Total:", fontSize = 12.sp)
                            Text("$${String.format("%,.0f", ventaSeleccionada!!.total)}", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        }
                        Column {
                            Text("Fecha:", fontSize = 12.sp)
                            Text(ventaSeleccionada!!.fecha.split("T")[0], fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("La factura en PDF se envía automáticamente al correo del cliente registrado.", fontSize = 11.sp, color = Color.Gray)
                }
            }
        )
    }
}
