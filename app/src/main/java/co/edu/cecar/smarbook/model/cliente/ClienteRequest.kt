package co.edu.cecar.smarbook.model.cliente

import kotlinx.serialization.Serializable

@Serializable
data class ClienteRequest(
    val identificacion: String,
    val nombres: String,
    val email: String,
    val celular: String,
    val fechaNacimiento: String
)
