package co.edu.cecar.smarbook.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaMas(
    opcionSeleccionada: String,
    onSeleccionarOpcion: (String) -> Unit
){
    val opciones = listOf(
        "Lotes",
        "Ingresos",
        "Inventarios",
        "Usuarios"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        opciones.forEach { opcion ->
            Button(
                onClick = {
                    onSeleccionarOpcion(opcion)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(opcion)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (opcionSeleccionada) {
            "Lotes" -> Text("Pantalla lotes ")
            "Ingresos" -> Text("Pantalla Ingresos")
            "Inventarios" -> Text("Pantalla Inventarios")
            "Usuarios" -> Text("Pantalla Usuarios")
        }
    }
}