package co.edu.cecar.smarbook.model.inventario

import kotlinx.serialization.Serializable

@Serializable
data class InventarioRequest(
    val lote : Int
)
