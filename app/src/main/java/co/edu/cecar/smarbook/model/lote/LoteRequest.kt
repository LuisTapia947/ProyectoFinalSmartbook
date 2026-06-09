package co.edu.cecar.smarbook.model.lote

import kotlinx.serialization.Serializable

@Serializable
data class LoteRequest(

    val lote: Int
)