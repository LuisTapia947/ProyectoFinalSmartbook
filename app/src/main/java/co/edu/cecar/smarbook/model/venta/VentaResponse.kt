package co.edu.cecar.smarbook.model.venta

import kotlinx.serialization.Serializable

@Serializable
data class VentaResponse(
    val id: Int,
    val numeroRecibo: String,
    val numeroComprobante: String,
    val total: Double,
    val fecha: String,
    val clienteNombre: String,
    val items: List<VentaItemDetalleResponse>? = null
)

@Serializable
data class VentaItemDetalleResponse(
    val libroId: Int,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double
)
