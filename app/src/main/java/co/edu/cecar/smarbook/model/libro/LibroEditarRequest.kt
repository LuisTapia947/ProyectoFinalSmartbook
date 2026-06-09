package co.edu.cecar.smarbook.model.libro

import kotlinx.serialization.Serializable

@Serializable
data class LibroEditarRequest(

    val nombre: String,

    val nivel: String,

    val tipo: Int,

    val edicion: String
)