package co.edu.cecar.smarbook.model.usuario

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioEditarRequest(
    val nombres: String,
    val email: String,
    val rol: Int,
    val activo: Boolean
)

@Serializable
data class MensajeResponseUsuario (
    val mensaje: String
)