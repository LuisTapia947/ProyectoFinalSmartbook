package co.edu.cecar.smarbook.model.Login

import kotlinx.serialization.Serializable

@Serializable
data class RestablecimientoRequest(
    val email: String
)

@Serializable
data class RestablecimientoResponse(
    val mensaje: String
)

@Serializable
data class NuevaContrasenaRequest(
    val codigo: String,
    val newPassword: String
)
