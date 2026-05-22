package co.edu.cecar.smarbook.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Divider
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
import co.edu.cecar.smarbook.component.DialogEditarCliente
import co.edu.cecar.smarbook.component.ItemListado
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.ClienteRepository
import co.edu.cecar.smarbook.model.cliente.ClienteResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun PantallaClientes() {

    var mostrarEditarState by remember {
        mutableStateOf(false)
    }

    var clienteSeleccionado by remember {
        mutableStateOf<ClienteResponse?>(null)
    }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { ClienteRepository() }

    val scope = rememberCoroutineScope()

    var buscar by remember {
        mutableStateOf("")
    }

    var listaClientes by remember {
        mutableStateOf<List<ClienteResponse>>(emptyList())
    }

    var mensajeError by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {

        val token = sessionManager.getToken().first()

        if (token != null) {

            repository.listarClientes(token)
                .onSuccess { response ->

                    listaClientes = response
                }
                .onFailure {

                    mensajeError =
                        "Error al cargar clientes"
                }
        }
    }

    val clientesFiltrados =
        listaClientes.filter { cliente ->

            cliente.nombres.contains(
                buscar,
                ignoreCase = true
            ) ||

                    cliente.identificacion.contains(
                        buscar,
                        ignoreCase = true
                    )
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Text(
            text = "Gestión de Clientes",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Administra los clientes del sistema SmartBook"
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

            Text("Nuevo Cliente")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mensajeError.isNotEmpty()) {

            Text(
                text = mensajeError,
                color = Color.Red
            )
        }

        LazyColumn {

            items(clientesFiltrados) { cliente ->

                ItemListado(

                    titulo = cliente.identificacion,

                    datos = listOf(
                        cliente.nombres,
                        cliente.email,
                        cliente.celular
                    ),

                    onEditar = {

                        clienteSeleccionado = cliente

                        mostrarEditarState = true
                    }
                )
            }
        }

        if (
            mostrarEditarState &&
            clienteSeleccionado != null
        ) {

            DialogEditarCliente(

                cliente = clienteSeleccionado!!,

                onDismiss = {

                    mostrarEditarState = false
                },

                onGuardar = { clienteEditado ->

                    scope.launch {

                        val token =
                            sessionManager
                                .getToken()
                                .first()

                        if (
                            token != null &&
                            clienteSeleccionado != null
                        ) {

                            repository.editarCliente(
                                token = token,
                                identificacion =
                                    clienteSeleccionado!!
                                        .identificacion,
                                cliente = clienteEditado
                            )
                                .onSuccess { response ->

                                    Toast.makeText(
                                        context,
                                        response.message,
                                        Toast.LENGTH_LONG
                                    ).show()

                                    repository
                                        .listarClientes(token)
                                        .onSuccess {
                                            listaClientes = it
                                        }

                                    mostrarEditarState =
                                        false
                                }
                        }
                    }
                }
            )
        }
    }
}