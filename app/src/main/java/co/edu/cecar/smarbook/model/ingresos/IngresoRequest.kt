package co.edu.cecar.smarbook.model.ingresos

import kotlinx.serialization.Serializable

@Serializable
data class IngresoRequest(

    val libroId: Int,

    val unidades: Int,

    val lote: Int,

    val valorCompra: Double,

    val valorVentaPublico: Double
)