package co.edu.cecar.smarbook.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smarbook.component.CardDashboard
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.data.repository.DashboadRepository
import co.edu.cecar.smarbook.model.dashboart.DashboardResponse
import kotlinx.coroutines.flow.first
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaDashboard(
    navegarAPantallaCliente : () -> Unit,
    navegarAPantallaLibro : () -> Unit,
    navegarAPantallaVenta : () -> Unit
){
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { DashboadRepository() }



    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
            maximumFractionDigits = 0
        }
    }

    var dashboard by remember { mutableStateOf<DashboardResponse?>(null) }
    var mensajeError by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {

        repository.obtenerDashboard(
            sessionManager.getToken().first()!!
        )
            .onSuccess { response ->
                dashboard = response
            }
            .onFailure {
                mensajeError = "Error al cargar dashboard"
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        Text(
            "Dashboard",
            color = Color.Black,
            fontSize = 19.sp,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center

        )
        Text(
            text = """
                Administra los clientes del sistema SmartBook""".trimIndent()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (mensajeError.isNotEmpty()) {
            Text(
                text = mensajeError,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        CardDashboard(
            titulo = "Total Clientes",
            valor = dashboard?.totalClientes?.toString() ?: "0",
            icono = Icons.Default.Person,
            colorLinea = Color.Red
        )

        Spacer(modifier = Modifier.height(10.dp))

        CardDashboard(
            titulo = "Libros Registrados",
            valor = dashboard?.totalLibros?.toString() ?: "0",
            icono = Icons.Default.MenuBook,
            colorLinea = Color(0xFF3F3CA8)
        )

        Spacer(modifier = Modifier.height(10.dp))

        CardDashboard(
            titulo = "Ventas Mes",
            valor = dashboard?.cantVentasMes?.toString() ?: "0",
            icono = Icons.Default.ShoppingBag,
            colorLinea = Color.Red
        )

        Spacer(modifier = Modifier.height(10.dp))

        CardDashboard(
            titulo = "Ingresos Mes",
            valor = currencyFormat.format(dashboard?.totalVentasMes ?: 0.0),
            icono = Icons.Default.AttachMoney,
            colorLinea = Color(0xFF3F3CA8)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Últimas ventas de hoy", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(12.dp))

                if (dashboard?.ventasHoy.isNullOrEmpty()) {
                    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "No hay ventas registradas hoy",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    dashboard?.ventasHoy?.forEach { venta ->

                        val fechaFormateada = remember(venta.fecha) {
                            LocalDateTime
                                .parse(venta.fecha)
                                .format(
                                    DateTimeFormatter.ofPattern("hh:mm a")
                                )
                        }

                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = venta.numeroRecibo,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = currencyFormat.format(venta.total),
                                    color = Color.Red
                                )

                                Text(
                                    text = fechaFormateada,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información general", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Bienvenido al panel administrativo de SmartBook. Desde aquí podrás gestionar todos los módulos del sistema."
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = Color(0xFFFFF5F5)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Acciones rápidas",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Button(
                                onClick = {navegarAPantallaCliente()},
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text(
                                    "Agregar cliente",
                                    fontSize = 10.sp,
                                    maxLines = 2
                                )
                            }

                            Button(
                                onClick = {navegarAPantallaLibro()},
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3F3CA8)
                                )
                            ) {
                                Text(
                                    "Registrar libro",
                                    fontSize = 10.sp,
                                    maxLines = 2
                                )
                            }

                            Button(
                                onClick = {navegarAPantallaVenta()},
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text(
                                    "Nueva venta",
                                    fontSize = 10.sp,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

