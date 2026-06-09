package co.edu.cecar.smarbook.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.IngresoRepository
import co.edu.cecar.smarbook.model.ingresos.IngresoResponse
import kotlinx.coroutines.flow.first
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.shape.RoundedCornerShape
import co.edu.cecar.smarbook.component.DialogCrearIngreso
import androidx.compose.material.icons.filled.*
import co.edu.cecar.smarbook.component.ItemDato
import co.edu.cecar.smarbook.component.ItemListado
import co.edu.cecar.smarbook.data.repository.LibroRepository
import co.edu.cecar.smarbook.data.repository.LoteRepository
import co.edu.cecar.smarbook.model.libro.LibroResponse
import co.edu.cecar.smarbook.model.lote.LoteResponse
import kotlinx.coroutines.launch
import java.util.Locale

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaIngresos() {

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { IngresoRepository() }
    val libroRepository = remember { LibroRepository() }
    val loteRepository = remember { LoteRepository() }
    val scope = rememberCoroutineScope()

    var listaIngresos by remember { mutableStateOf<List<IngresoResponse>>(emptyList()) }
    var listaLibros by remember { mutableStateOf<List<LibroResponse>>(emptyList()) }
    var listaLotes by remember { mutableStateOf<List<LoteResponse>>(emptyList()) }
    var buscar by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    var mostrarDialogCrear by remember { mutableStateOf(false) }

    fun cargarDatos() {
        scope.launch {
            val token = sessionManager.getToken().first()
            if (token != null) {
                repository.listarIngresos(token)
                    .onSuccess { 
                        listaIngresos = it 
                        mensajeError = ""
                    }
                    .onFailure { mensajeError = "Error al cargar ingresos" }

                libroRepository.listarLibros(token)
                    .onSuccess { listaLibros = it }

                loteRepository.listarLotes(token)
                    .onSuccess { listaLotes = it }
            }
        }
    }

    LaunchedEffect(Unit) {
        cargarDatos()
    }

    val ingresosFiltrados = listaIngresos.filter {
        it.lote.toString().contains(buscar, ignoreCase = true)
    }

    val totalIngresos = listaIngresos.sumOf { it.valorVentaPublico * it.unidades }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogCrear = true },
                containerColor = Color.Red,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Ingreso")
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
            Text(
                text = "Ingresos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Gestión de ingresos del sistema")

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Red),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Total generado", color = Color.White, fontSize = 14.sp)
                    Text(
                        text = "$ ${String.format(Locale.getDefault(), "%,.2f", totalIngresos)}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = buscar,
                onValueChange = { buscar = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por lote") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (mensajeError.isNotEmpty()) {
                Text(text = mensajeError, color = Color.Red)
                Spacer(modifier = Modifier.height(10.dp))
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                ingresosFiltrados.forEach { ingreso ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        ItemListado(
                            titulo = "Lote: ${ingreso.lote}",
                            datos = listOf(
                                ItemDato(Icons.Default.CalendarToday, "Fecha: ${ingreso.fecha.split("T")[0]}"),
                                ItemDato(Icons.Default.Inventory, "Unidades: ${ingreso.unidades}"),
                                ItemDato(Icons.Default.AccountBalanceWallet, "Valor Compra: $${String.format("%,.0f", ingreso.valorCompra)}"),
                                ItemDato(Icons.Default.Label, "Valor Venta: $${String.format("%,.0f", ingreso.valorVentaPublico)}")
                            ),
                            onEditar = { /* Editar ingreso */ }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (mostrarDialogCrear) {
        DialogCrearIngreso(
            libros = listaLibros,
            lotes = listaLotes,
            onDismiss = { mostrarDialogCrear = false },
            onGuardar = { request ->
                scope.launch {
                    val token = sessionManager.getToken().first()
                    if (token != null) {
                        repository.crearIngreso(token, request)
                            .onSuccess {
                                Toast.makeText(context, "Ingreso creado correctamente", Toast.LENGTH_LONG).show()
                                cargarDatos()
                                mostrarDialogCrear = false
                            }
                            .onFailure { error ->
                                Toast.makeText(context, error.message ?: "Error al crear ingreso", Toast.LENGTH_LONG).show()
                            }
                    }
                }
            }
        )
    }
}
