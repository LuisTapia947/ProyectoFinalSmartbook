package co.edu.cecar.smarbook.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> Dropdow(
    items: List<T>,
    selectedItem: T? = null,
    onItemSelected: ((T) -> Unit)? = null,
    placeholder: String = "Selecciona una opción",
    label: String? = null,
    itemLabel: (T) -> String = { it.toString() },
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedItem?.let { itemLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = label?.let {
                { Text(it) }
            },
            placeholder = {
                Text(placeholder)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(itemLabel(item))
                    },
                    onClick = {
                        onItemSelected?.invoke(item)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}