package co.edu.cecar.smarbook.screen.libro

import android.widget.Toast
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
import co.edu.cecar.smarbook.component.DialogEditarLibro
import co.edu.cecar.smarbook.component.DialogCrearLibro
import co.edu.cecar.smarbook.component.ItemDato
import co.edu.cecar.smarbook.component.ItemListado
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.LibroRepository
import co.edu.cecar.smarbook.model.libro.LibroEditarRequest
import co.edu.cecar.smarbook.model.libro.LibroRequest
import co.edu.cecar.smarbook.model.libro.LibroResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLibros() {
    var mostrarDialogNuevo by remember { mutableStateOf(false) }
    var mostrarDialogEditar by remember { mutableStateOf(false) }
    var libroSeleccionado by remember { mutableStateOf<LibroResponse?>(null) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { LibroRepository() }
    val scope = rememberCoroutineScope()

    var buscar by remember { mutableStateOf("") }
    var listaLibros by remember { mutableStateOf<List<LibroResponse>>(emptyList()) }
    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(true) }

    suspend fun cargarLibros() {
        val token = sessionManager.getToken().first()
        if (token != null) {
            repository.listarLibros(token)
                .onSuccess { 
                    listaLibros = it 
                    cargando = false
                }
                .onFailure { 
                    mensajeError = "Error al cargar libros"
                    cargando = false
                }
        }
    }

    LaunchedEffect(Unit) {
        cargarLibros()
    }

    val librosFiltrados = listaLibros.filter { libro ->
        libro.nombre.contains(buscar, ignoreCase = true) ||
        libro.nivel.contains(buscar, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogNuevo = true },
                containerColor = Color.Red,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Libro")
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
            Text(text = "Gestión de Libros", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "Administra los libros del sistema SmartBook")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = buscar,
                onValueChange = { buscar = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por nombre o nivel") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (mensajeError.isNotEmpty()) {
                Text(text = mensajeError, color = Color.Red)
            }

            if (cargando) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    librosFiltrados.forEach { libro ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            ItemListado(
                                titulo = libro.nombre,
                                datos = listOf(
                                    ItemDato(Icons.Default.Layers, "Nivel: ${libro.nivel}"),
                                    ItemDato(Icons.Default.Info, "Edición: ${libro.edicion}"),
                                    ItemDato(Icons.Default.Book, "Tipo: ${libro.tipo}"),
                                    ItemDato(Icons.Default.QrCode, "Lote: ${libro.lote}"),
                                    ItemDato(Icons.Default.Storage, "Stock: ${libro.stockTotal}")
                                ),
                                onEditar = {
                                    libroSeleccionado = libro
                                    mostrarDialogEditar = true
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (mostrarDialogNuevo) {
        DialogCrearLibro(
            onDismiss = { mostrarDialogNuevo = false },
            onGuardar = { libroRequest ->
                scope.launch {
                    val token = sessionManager.getToken().first()
                    if (token != null) {
                        repository.crearLibro(token, libroRequest)
                            .onSuccess {
                                Toast.makeText(context, "Libro creado correctamente", Toast.LENGTH_LONG).show()
                                cargarLibros()
                                mostrarDialogNuevo = false
                            }
                            .onFailure {
                                Toast.makeText(context, "Error al crear libro", Toast.LENGTH_LONG).show()
                            }
                    }
                }
            }
        )
    }

    if (mostrarDialogEditar && libroSeleccionado != null) {
        DialogEditarLibro(
            libro = libroSeleccionado!!,
            onDismiss = { mostrarDialogEditar = false },
            onGuardar = { libroEditado ->
                scope.launch {
                    val token = sessionManager.getToken().first()
                    if (token != null) {
                        repository.editarLibro(token, libroSeleccionado!!.id, libroEditado)
                            .onSuccess {
                                Toast.makeText(context, "Libro actualizado correctamente", Toast.LENGTH_LONG).show()
                                cargarLibros()
                                mostrarDialogEditar = false
                            }
                            .onFailure {
                                Toast.makeText(context, "Error al editar libro", Toast.LENGTH_LONG).show()
                            }
                    }
                }
            }
        )
    }
}
