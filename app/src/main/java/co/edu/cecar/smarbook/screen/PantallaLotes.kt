package co.edu.cecar.smarbook.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.LoteRepository
import co.edu.cecar.smarbook.model.lote.LoteResponse
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.component.DialogCrearLote
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun PantallaLotes() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { LoteRepository() }
    val scope = rememberCoroutineScope()

    var buscar by remember { mutableStateOf("") }
    var listaLotes by remember { mutableStateOf<List<LoteResponse>>(emptyList()) }
    var mensajeError by remember { mutableStateOf("") }
    var mostrarDialogCrear by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val token = sessionManager.getToken().first()
        if (token != null) {
            repository.listarLotes(token)
                .onSuccess { listaLotes = it }
                .onFailure { mensajeError = "Error al cargar lotes" }
        }
    }

    val lotesFiltrados = listaLotes.filter {
        it.codigo.toString().contains(buscar, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogCrear = true },
                containerColor = Color.Red,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Lote")
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
                text = "Gestión de Lotes",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Administra los lotes del sistema SmartBook"
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = buscar,
                onValueChange = { buscar = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar lote") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (mensajeError.isNotEmpty()) {
                Text(text = mensajeError, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEAEAEA))
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "Código",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Estado",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }

                    HorizontalDivider()

                    Column {
                        lotesFiltrados.forEach { lote ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = lote.codigo.toString(),
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = if (lote.actual) "Activo" else "Inactivo",
                                    color = if (lote.actual) Color(0xFF2E7D32) else Color.Red,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (mostrarDialogCrear) {
        DialogCrearLote(
            onDismiss = { mostrarDialogCrear = false },
            onGuardar = { request ->
                scope.launch {
                    val token = sessionManager.getToken().first()
                    if (token != null) {
                        repository.crearLote(token, request)
                            .onSuccess {
                                Toast.makeText(context, "Lote creado correctamente", Toast.LENGTH_LONG).show()
                                repository.listarLotes(token).onSuccess { listaLotes = it }
                                mostrarDialogCrear = false
                            }
                            .onFailure {
                                Toast.makeText(context, "Error al crear lote", Toast.LENGTH_LONG).show()
                            }
                    }
                }
            }
        )
    }
}
