package co.edu.cecar.smarbook.screen

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.R
import co.edu.cecar.smarbook.component.MenuMas
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.screen.libro.PantallaLibros

data class BottonItem(
    val titleBottom: String,
    val titleTop: String,
    val icon: ImageVector,
    val soloAdmin: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMenuPrincipal(
    cerrarSesion: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val nombreUsuario by sessionManager.getNombre().collectAsState(initial = "")
    val rolUsuario by sessionManager.getRol().collectAsState(initial = "")

    var selectedItem by remember { mutableStateOf(0) }
    var seleccionarMenuState by remember { mutableStateOf(false) }
    var menuMasState by remember { mutableStateOf(false) }
    var pantallaExtra by remember { mutableStateOf("") }

    val allItems = listOf(
        BottonItem("Dashboard", titleTop = "Dashboard", Icons.Default.Dashboard),
        BottonItem("Clientes", titleTop = "Gestión de Clientes", Icons.Default.People),
        BottonItem("Libros", titleTop = "Gestión de Libros", Icons.Default.MenuBook, soloAdmin = true),
        BottonItem("Ventas", titleTop = "Gestión de Ventas", Icons.Default.ReceiptLong),
        BottonItem("Mas", titleTop = "Más opciones", Icons.Default.MoreHoriz),
    )

    // Filtrar items según el rol (si el rol no es "Admin", ocultar los que son "soloAdmin")
    val isUserAdmin = rolUsuario == "Admin"
    val items = if (isUserAdmin) allItems else allItems.filter { !it.soloAdmin }

    val activity = LocalActivity.current

    BackHandler {
        if (selectedItem != 0) {
            selectedItem = 0
            pantallaExtra = ""
        } else {
            activity?.finish()
        }
    }

    val tituloPantalla =
        if (selectedItem < items.size && items[selectedItem].titleBottom == "Mas" && pantallaExtra.isNotEmpty())
            pantallaExtra
        else if (selectedItem < items.size)
            items[selectedItem].titleTop
        else
            "SmartBook"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (selectedItem == 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.cdilogo2022),
                                contentDescription = "Logo CDI",
                                modifier = Modifier
                                    .width(180.dp)
                                    .height(62.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    } else {
                        Text(text = tituloPantalla)
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = nombreUsuario ?: "",
                                color = Color.Black,
                                fontSize = 12.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isUserAdmin) "Administrador" else "Vendedor",
                                color = Color.Gray,
                                fontSize = 10.sp
                            )
                        }
                        Box {
                            IconButton(onClick = { seleccionarMenuState = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Opciones",
                                    tint = Color.Black
                                )
                            }
                            DropdownMenu(
                                expanded = seleccionarMenuState,
                                onDismissRequest = { seleccionarMenuState = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Cerrar sesión") },
                                    onClick = {
                                        seleccionarMenuState = false
                                        cerrarSesion()
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            if (item.titleBottom == "Mas") {
                                menuMasState = true
                            } else {
                                selectedItem = index
                                pantallaExtra = ""
                            }
                        },
                        icon = {
                            Box {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.titleBottom
                                )
                                if (item.titleBottom == "Mas") {
                                    MenuMas(
                                        expanded = menuMasState,
                                        onDismiss = { menuMasState = false },
                                        onSeleccionar = { opcion ->
                                            val masIndex = items.indexOfFirst { it.titleBottom == "Mas" }
                                            if (masIndex != -1) {
                                                selectedItem = masIndex
                                                pantallaExtra = opcion
                                            }
                                        }
                                    )
                                }
                            }
                        },
                        label = { Text(item.titleBottom) }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val currentItemTitle = if (selectedItem < items.size) items[selectedItem].titleBottom else ""
            
            when {
                currentItemTitle == "Dashboard" -> PantallaDashboard(
                    navegarAPantallaCliente = {
                        val index = items.indexOfFirst { it.titleBottom == "Clientes" }
                        if (index != -1) { selectedItem = index; pantallaExtra = "" }
                    },
                    navegarAPantallaLibro = {
                        val index = items.indexOfFirst { it.titleBottom == "Libros" }
                        if (index != -1) { selectedItem = index; pantallaExtra = "" }
                    },
                    navegarAPantallaVenta = {
                        val index = items.indexOfFirst { it.titleBottom == "Ventas" }
                        if (index != -1) { selectedItem = index; pantallaExtra = "" }
                    }
                )
                currentItemTitle == "Clientes" -> PantallaClientes()
                currentItemTitle == "Libros" -> PantallaLibros()
                currentItemTitle == "Ventas" -> PantallaVentas()
                currentItemTitle == "Mas" -> {
                    when (pantallaExtra) {
                        "Lotes" -> PantallaLotes()
                        "Ingresos" -> PantallaIngresos()
                        "Inventarios" -> PantallaInventarios()
                        "Usuarios" -> if (isUserAdmin) PantallaUsuarios() else Text("No tienes acceso")
                        else -> Text("Seleccione una opción")
                    }
                }
                else -> Text("Cargando...")
            }
        }
    }
}
