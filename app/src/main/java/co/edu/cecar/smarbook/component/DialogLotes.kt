package co.edu.cecar.smarbook.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import co.edu.cecar.smarbook.model.lote.LoteRequest

@Composable
fun DialogCrearLote(
    onDismiss: () -> Unit,
    onGuardar: (LoteRequest) -> Unit
) {
    var loteState by remember { mutableStateOf("") }
    var errorValidacion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Nuevo Lote", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                if (errorValidacion.isNotEmpty()) {
                    Text(text = errorValidacion, color = Color.Red)
                }
                OutlinedTextField(
                    value = loteState,
                    onValueChange = { loteState = it },
                    label = { Text("Número de Lote") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val loteNum = loteState.toIntOrNull()
                    if (loteNum != null) {
                        onGuardar(LoteRequest(lote = loteNum))
                    } else {
                        errorValidacion = "Ingrese un número válido"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
