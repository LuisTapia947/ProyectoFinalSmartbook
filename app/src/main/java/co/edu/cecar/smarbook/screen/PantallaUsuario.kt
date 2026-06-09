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
import co.edu.cecar.smarbook.component.DialogCrearUsuario
import co.edu.cecar.smarbook.component.DialogEditarUsuario
import co.edu.cecar.smarbook.component.ItemDato
import co.edu.cecar.smarbook.component.ItemListado
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.UsuarioRepository
import co.edu.cecar.smarbook.model.usuario.UsuarioResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaUsuarios() {
    var mostrarCrearUsuarioState by remember { mutableStateOf(false) }
    var mostrarEditarUsuarioState by remember { mutableStateOf(false) }
    var usuarioSeleccionado by remember { mutableStateOf<UsuarioResponse?>(null) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { UsuarioRepository() }
    val scope = rememberCoroutineScope()

    var buscar by remember { mutableStateOf("") }
    var listaUsuarios by remember { mutableStateOf<List<UsuarioResponse>>(emptyList()) }
    var mensajeError by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val token = sessionManager.getToken().first()
        if (token != null) {
            repository.listarUsuarios(token)
                .onSuccess { listaUsuarios = it }
                .onFailure { mensajeError = "Error al cargar usuarios" }
        }
    }

    val usuarioFiltrados = listaUsuarios.filter { usuario ->
        usuario.nombres.contains(buscar, ignoreCase = true) ||
        usuario.identificacion.contains(buscar, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    mensajeError = ""
                    mostrarCrearUsuarioState = true 
                },
                containerColor = Color.Red,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Usuario")
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
            Text(text = "Gestión de Usuarios", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "Administra los Usuarios del sistema SmartBook")

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
                usuarioFiltrados.forEach { usuario ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        ItemListado(
                            titulo = usuario.identificacion,
                            datos = listOf(
                                ItemDato(Icons.Default.Person, "Nombre: ${usuario.nombres}"),
                                ItemDato(Icons.Default.Email, "Email: ${usuario.email}"),
                                ItemDato(Icons.Default.Badge, "Rol: ${if (usuario.rol == "Admin" || usuario.rol == "2") "Administrador" else "Vendedor"}"),
                                ItemDato(if (usuario.activo) Icons.Default.CheckCircle else Icons.Default.Cancel, "Estado: ${if (usuario.activo) "Activo" else "Inactivo"}")
                            ),
                            onEditar = {
                                usuarioSeleccionado = usuario
                                mensajeError = ""
                                mostrarEditarUsuarioState = true
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (mostrarEditarUsuarioState && usuarioSeleccionado != null) {
        DialogEditarUsuario(
            usuario = usuarioSeleccionado!!,
            mensajeError = mensajeError,
            onDismiss = {
                mostrarEditarUsuarioState = false
                usuarioSeleccionado = null
                mensajeError = ""
            },
            onGuardar = { usuarioEditado ->
                scope.launch {
                    val token = sessionManager.getToken().first()
                    if (token != null && usuarioSeleccionado != null) {
                        repository.editarUsuario(
                            token = token,
                            identificacion = usuarioSeleccionado!!.id,
                            usuario = usuarioEditado
                        )
                            .onSuccess { response ->
                                Toast.makeText(context, response.mensaje, Toast.LENGTH_LONG).show()
                                repository.listarUsuarios(token).onSuccess { listaUsuarios = it }
                                mostrarEditarUsuarioState = false
                            }
                            .onFailure { error ->
                                mensajeError = error.message ?: "Error al actualizar usuario"
                            }
                    }
                }
            }
        )
    }

    if (mostrarCrearUsuarioState) {
        DialogCrearUsuario(
            mensajeError = mensajeError,
            onDismiss = {
                mostrarCrearUsuarioState = false
                mensajeError = ""
            },
            onGuardar = { nuevoUsuario ->
                scope.launch {
                    val token = sessionManager.getToken().first()
                    if (token != null) {
                        repository.crearUsuario(token, nuevoUsuario)
                            .onSuccess { response ->
                                Toast.makeText(context, response.mensaje, Toast.LENGTH_LONG).show()
                                repository.listarUsuarios(token).onSuccess { listaUsuarios = it }
                                mostrarCrearUsuarioState = false
                                mensajeError = ""
                            }
                            .onFailure { error ->
                                mensajeError = error.message ?: "Error al crear usuario"
                            }
                    } else {
                        mensajeError = "No hay token de sesión"
                    }
                }
            }
        )
    }
}
