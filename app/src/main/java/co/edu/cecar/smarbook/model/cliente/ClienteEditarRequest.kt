package co.edu.cecar.smarbook.model.cliente

import kotlinx.serialization.Serializable
@Serializable
data class ClienteEditarRequest(
    val nombres: String,
    val email: String,
    val celular: String,
    val fechaNacimiento: String
)
@Serializable
data class MensajeResponse(
    val message: String
)