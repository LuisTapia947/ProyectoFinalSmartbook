package co.edu.cecar.smarbook.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import co.edu.cecar.smarbook.component.Dropdow
import co.edu.cecar.smarbook.component.ItemDato
import co.edu.cecar.smarbook.component.ItemListado
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.InventarioRepository
import co.edu.cecar.smarbook.data.repository.LoteRepository
import co.edu.cecar.smarbook.model.inventario.InventarioResponse
import co.edu.cecar.smarbook.model.lote.LoteResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInventarios() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { InventarioRepository() }
    val loteRepository = remember { LoteRepository() }
    val scope = rememberCoroutineScope()

    var cargando by remember { mutableStateOf(true) }
    var buscar by remember { mutableStateOf("") }
    var loteSeleccionado by remember { mutableStateOf<LoteResponse?>(null) }
    
    var listaInventario by remember { mutableStateOf<List<InventarioResponse>>(emptyList()) }
    var listaLotes by remember { mutableStateOf<List<LoteResponse>>(emptyList()) }
    var mensajeError by remember { mutableStateOf("") }

    fun cargarInventario() {
        scope.launch {
            cargando = true
            val token = sessionManager.getToken().first()
            if (token != null) {
                repository.listarInventario(token, loteSeleccionado?.codigo)
                    .onSuccess {
                        listaInventario = it
                        mensajeError = ""
                    }
                    .onFailure {
                        mensajeError = it.message ?: "No se pudo cargar el inventario"
                    }
            }
            cargando = false
        }
    }

    LaunchedEffect(Unit) {
        val token = sessionManager.getToken().first()
        if (token != null) {
            launch {
                loteRepository.listarLotes(token).onSuccess { 
                    listaLotes = it 
                }
            }
        }
    }

    // Recargar inventario cuando cambie el lote seleccionado
    LaunchedEffect(loteSeleccionado) {
        cargarInventario()
    }

    val inventarioFiltrados = listaInventario.filter {
        it.nombreLibro.contains(buscar, ignoreCase = true)
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Inventario de Libros",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Consulta el stock disponible por lote"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card de Filtros
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Dropdow(
                            items = listaLotes,
                            selectedItem = loteSeleccionado,
                            onItemSelected = { loteSeleccionado = it },
                            label = "SELECCIONAR LOTE",
                            placeholder = "Todos los lotes",
                            itemLabel = { it.codigo.toString() },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    OutlinedTextField(
                        value = buscar,
                        onValueChange = { buscar = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar por nombre de libro") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (mensajeError.isNotEmpty()) {
                Text(text = mensajeError, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
            }

            if (cargando) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Red)
                }
            } else if (inventarioFiltrados.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Inventory, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
                        Text(
                            text = if (loteSeleccionado == null) "Mostrando inventario general" else "No hay resultados para este lote", 
                            color = Color.Gray
                        )
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    inventarioFiltrados.forEach { libro ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            ItemListado(
                                titulo = libro.nombreLibro,
                                datos = listOf(
                                    ItemDato(Icons.Default.Layers, "Nivel: ${libro.nivelLibro} (${libro.edicionLibro})"),
                                    ItemDato(Icons.Default.Book, "Tipo: ${libro.tipoLibro}"),
                                    ItemDato(Icons.Default.Input, "Ingresados: ${libro.cantidadIngresada}"),
                                    ItemDato(Icons.Default.Sell, "Vendidos: ${libro.cantidadVendida}"),
                                    ItemDato(Icons.Default.Inventory2, "Stock Disponible: ${libro.stockDisponible}")
                                ),
                                onEditar = { }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
