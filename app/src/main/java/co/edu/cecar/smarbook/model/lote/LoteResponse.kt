package co.edu.cecar.smarbook.model.lote

import kotlinx.serialization.Serializable

@Serializable
data class LoteResponse(

    val codigo: Int,

    val actual: Boolean
)