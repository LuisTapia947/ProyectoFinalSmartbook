package co.edu.cecar.smarbook.model.venta

import kotlinx.serialization.Serializable

@Serializable
data class VentaRequest(
    val identificacionCliente: String,
    val numeroComprobante: String,
    val observaciones: String,
    val items: List<VentaItemRequest>
)

@Serializable
data class VentaItemRequest(
    val libroId: Int,
    val lote: Int,
    val cantidad: Int
)
