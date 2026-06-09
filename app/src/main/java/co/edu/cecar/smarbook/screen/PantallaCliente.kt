package co.edu.cecar.smarbook.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.component.DialogCrearCliente
import co.edu.cecar.smarbook.component.DialogEditarCliente
import co.edu.cecar.smarbook.component.ItemDato
import co.edu.cecar.smarbook.component.ItemListado
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.ClienteRepository
import co.edu.cecar.smarbook.model.cliente.ClienteResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaClientes() {
    var mostrarEditarState by remember { mutableStateOf(false) }
    var mostrarCrearClienteState by remember { mutableStateOf(false) }
    var clienteSeleccionado by remember { mutableStateOf<ClienteResponse?>(null) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { ClienteRepository() }
    val scope = rememberCoroutineScope()

    var buscar by remember { mutableStateOf("") }
    var listaClientes by remember { mutableStateOf<List<ClienteResponse>>(emptyList()) }
    var mensajeError by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val token = sessionManager.getToken().first()
        if (token != null) {
            repository.listarClientes(token)
                .onSuccess { listaClientes = it }
                .onFailure { mensajeError = "Error al cargar clientes" }
        }
    }

    val clientesFiltrados = listaClientes.filter { cliente ->
        cliente.nombres.contains(buscar, ignoreCase = true) ||
        cliente.identificacion.contains(buscar, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    mensajeError = ""
                    mostrarCrearClienteState = true 
                },
                containerColor = Color.Red,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Cliente")
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
            Text(text = "Gestión de Clientes", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "Administra los clientes del sistema SmartBook")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = buscar,
                onValueChange = { buscar = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por identificación o nombre") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (mensajeError.isNotEmpty()) {
                Text(text = mensajeError, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                clientesFiltrados.forEach { cliente ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        ItemListado(
                            titulo = cliente.identificacion,
                            datos = listOf(
                                ItemDato(Icons.Default.Person, "Nombres: ${cliente.nombres}"),
                                ItemDato(Icons.Default.Email, "Email: ${cliente.email}"),
                                ItemDato(Icons.Default.Phone, "Celular: ${cliente.celular}")
                            ),
                            onEditar = {
                                clienteSeleccionado = cliente
                                mostrarEditarState = true
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (mostrarEditarState && clienteSeleccionado != null) {
        DialogEditarCliente(
            cliente = clienteSeleccionado!!,
            onDismiss = { mostrarEditarState = false },
            onGuardar = { clienteEditado ->
                scope.launch {
                    val token = sessionManager.getToken().first()
                    if (token != null && clienteSeleccionado != null) {
                        repository.editarCliente(
                            token = token,
                            identificacion = clienteSeleccionado!!.identificacion,
                            cliente = clienteEditado
                        )
                            .onSuccess { response ->
                                Toast.makeText(context, response.mensaje, Toast.LENGTH_LONG).show()
                                repository.listarClientes(token).onSuccess { listaClientes = it }
                                mostrarEditarState = false
                            }
                    }
                }
            }
        )
    }

    if (mostrarCrearClienteState) {
        DialogCrearCliente(
            mensajeError = mensajeError,
            onDismiss = { 
                mostrarCrearClienteState = false 
                mensajeError = ""
            },
            onGuardar = { nuevoCliente ->
                scope.launch {
                    val token = sessionManager.getToken().first()
                    if (token != null) {
                        repository.crearCliente(token, nuevoCliente)
                            .onSuccess {
                                Toast.makeText(context, "Cliente registrado con éxito", Toast.LENGTH_LONG).show()
                                repository.listarClientes(token).onSuccess { listaClientes = it }
                                mostrarCrearClienteState = false
                                mensajeError = ""
                            }
                            .onFailure { error ->
                                mensajeError = error.message ?: "Error al crear cliente"
                            }
                    } else {
                        mensajeError = "No hay token de sesión"
                    }
                }
            }
        )
    }
}
