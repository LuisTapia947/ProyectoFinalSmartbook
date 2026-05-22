package co.edu.cecar.smarbook.screen

import android.app.Activity
import android.content.ClipData
import android.graphics.drawable.Icon
import android.icu.text.CaseMap
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.R
import co.edu.cecar.smarbook.component.MenuMas
import co.edu.cecar.smarbook.data.local.SessionManager

data class BottonItem(
    val titleBottom: String,
    val titleTop: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMenuPrincipal(
    cerrarSesion: () -> Unit

) {

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val nombreUsuario by sessionManager.getNombre().collectAsState(initial = "")

    var selectedItem by remember { mutableStateOf(0) }
    var seleccionarMenuState by remember { mutableStateOf(false) }
    var menuMasState by remember { mutableStateOf(false) }
    var pantallaExtra by remember { mutableStateOf("") }

    val items = listOf(
        BottonItem("Dashboard",titleTop = "Dashboard", Icons.Default.Dashboard),
        BottonItem("Clientes",titleTop = "Gestión de Clientes", Icons.Default.People),
        BottonItem("Libros",titleTop = "Gestión de Libros", Icons.Default.MenuBook),
        BottonItem("Ventas", titleTop = "Gestión de Ventas",Icons.Default.ReceiptLong),
        BottonItem("Mas",titleTop = "Más opciones", Icons.Default.MoreHoriz),
    )
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
        if (selectedItem == 4 && pantallaExtra.isNotEmpty())
            pantallaExtra
        else
            items[selectedItem].titleTop
    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    if (selectedItem == 0) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

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

                        Text(
                            text = tituloPantalla
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = nombreUsuario ?: "",
                            color = Color.Black,
                            fontSize = 12.sp,
                            maxLines = 1
                        )

                        Box {

                            IconButton(
                                onClick = {
                                    seleccionarMenuState = true
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Opciones",
                                    tint = Color.Black
                                )
                            }

                            DropdownMenu(
                                expanded = seleccionarMenuState,
                                onDismissRequest = {
                                    seleccionarMenuState = false
                                }
                            ) {

                                DropdownMenuItem(
                                    text = {
                                        Text("Cerrar sesión")
                                    },
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
                                        onDismiss = {
                                            menuMasState = false
                                        },
                                        onSeleccionar = { opcion ->
                                            selectedItem = 4
                                            pantallaExtra = opcion
                                        }
                                    )
                                }
                            }
                        },
                        label = {
                            Text(item.titleBottom)
                        }
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
            when (selectedItem) {
                0 -> PantallaDashboard(
                    navegarAPantallaCliente = {
                        selectedItem = 1
                        pantallaExtra = ""
                    },
                    navegarAPantallaLibro = {
                        selectedItem = 2
                        pantallaExtra = ""
                    },
                    navegarAPantallaVenta = {
                        selectedItem = 3
                        pantallaExtra = ""
                    }
                )

                1 -> PantallaClientes()
                2 -> PantallaLibros()
                3 -> PantallaVentas()
                4 -> {
                    when (pantallaExtra) {
                        "Lotes" -> PantallaLotes()
                        "Ingresos" -> PanllattaIngresos()
                        "Inventarios" -> PantallaInventarios()
                        "Usuarios" -> PantallaUsuarios()
                        else -> Text("Seleccione una opción")
                    }
                }
            }
        }
    }

}

