package co.edu.cecar.smarbook.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ItemDato(
    val icono: ImageVector,
    val valor: String
)

@Composable
fun ItemListado(
    titulo: String,
    datos: List<ItemDato>,
    textoAccion: String = "Editar",
    onEditar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            text = titulo,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        datos.forEach { dato ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Icon(
                    imageVector = dato.icono,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dato.valor,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        if (textoAccion.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = textoAccion,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { onEditar() }
                    .align(Alignment.End)
                    .padding(4.dp)
            )
        }
    }
}
