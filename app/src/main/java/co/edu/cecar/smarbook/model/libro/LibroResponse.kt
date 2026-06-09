package co.edu.cecar.smarbook.model.libro

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibroResponse(
    val id: Int,
    val nombre: String,
    val nivel: String,
    val tipo: String,
    val lote: Int,
    val edicion: String,
    val stockTotal: Int,
    
    @SerialName("valorCompa")
    val valorCompra: Double,
    
    @SerialName("valorVentaPulico")
    val valorVentaPublico: Double
)
