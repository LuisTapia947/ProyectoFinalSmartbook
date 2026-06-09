package co.edu.cecar.smarbook.model.libro

import kotlinx.serialization.Serializable

@Serializable
data class LibroRequest(

    val nombre: String,

    val nivel: String,

    val tipo: Int,

    val edicion: String,

    val unidades: Int,

    val lote: Int,

    val valorCompra: Double,

    val valorVentaPublico: Double
)

