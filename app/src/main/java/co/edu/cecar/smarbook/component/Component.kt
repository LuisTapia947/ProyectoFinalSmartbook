package co.edu.cecar.smarbook.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable



fun textoCodigo(correoState: String): String{
    if (correoState.isNotBlank()){
        return "Se ha enviado un enlace a tu correo"
    }
    return ""
}
@Composable
fun MenuMas(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSeleccionar: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        listOf("Lotes",
            "Ingresos",
            "Inventarios",
            "Usuarios").forEach { opcion ->
            DropdownMenuItem(
                text = { Text(opcion) },
                onClick = {
                    onSeleccionar(opcion)
                    onDismiss()
                }
            )
        }
    }
}
