package co.edu.cecar.smarbook.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.component.DialogEditarUsuario
import co.edu.cecar.smarbook.component.ItemListado
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.UsuarioRepository
import co.edu.cecar.smarbook.model.usuario.UsuarioResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun PantallaUsuarios() {
    var mostrarEditarUsuarioState by remember {
        mutableStateOf(false)
    }

    var usuarioSeleccionado by remember {
        mutableStateOf<UsuarioResponse?>(null)
    }

    val context = LocalContext.current

    val sessionManager = remember { SessionManager(context) }
    val repository = remember { UsuarioRepository() }

    val scope = rememberCoroutineScope()

    var buscar by remember {
        mutableStateOf("")
    }

    var listaUsuarios by remember {
        mutableStateOf<List<UsuarioResponse>>(emptyList())
    }

    var mensajeError by remember {
        mutableStateOf("")
    }

    fun cargarUsuarios() {
        scope.launch {
            val token = sessionManager.getToken().first()
            if (token != null) {
                repository.listarUsuarios(token)
                    .onSuccess { response ->
                        listaUsuarios = response
                        mensajeError = ""
                    }
                    .onFailure { error ->
                        mensajeError = error.message ?: "Error al cargar usuarios"
                    }
            }
        }
    }

    LaunchedEffect(Unit) {
        cargarUsuarios()
    }

    val usuarioFiltrados = listaUsuarios.filter { usuario ->
        usuario.nombres.contains(buscar, ignoreCase = true) ||
                usuario.identificacion.contains(buscar, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text(
            text = "Gestión de Usuarios",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Administra los Usuarios del sistema SmartBook"
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = buscar,
            onValueChange = {
                buscar = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Buscar por identificación o nombre"
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Lógica para nuevo usuario
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Nuevo Usuario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mensajeError.isNotEmpty()) {
            Text(
                text = mensajeError,
                color = Color.Red
            )
        }

        LazyColumn {
            items(usuarioFiltrados) { usuario ->
                ItemListado(
                    titulo = usuario.identificacion,
                    datos = listOf(
                        "Nombre: ${usuario.nombres}",
                        "Email: ${usuario.email}",
                        "Rol: ${if (usuario.rol == "Admin") "Administrador" else "Vendedor"}",
                        "Estado: ${if (usuario.activo) "Activo" else "Inactivo"}"
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

    if (mostrarEditarUsuarioState && usuarioSeleccionado != null) {
        DialogEditarUsuario(
            usuario = usuarioSeleccionado!!,
            mensajeError = mensajeError,
            onDismiss = {
                mostrarEditarUsuarioState = false
                usuarioSeleccionado = null
                mensajeError = ""
            },
            onGuardar = { request ->
                scope.launch {
                    val token = sessionManager.getToken().first()
                    if (token != null) {
                        repository.editarUsuario(
                            token = token,
                            identificacion = usuarioSeleccionado!!.id,
                            usuario = request
                        )
                            .onSuccess {response ->

                                Toast.makeText(
                                    context,
                                    response.mensaje,
                                    Toast.LENGTH_LONG
                                ).show()
                                mostrarEditarUsuarioState = false
                                usuarioSeleccionado = null
                                cargarUsuarios()
                            }
                            .onFailure { error ->
                                mensajeError = error.message ?: "Error al actualizar usuario"
                            }
                    }
                }
            }
        )
    }
}
