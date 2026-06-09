package co.edu.cecar.smarbook.model.usuario

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioRequest(
    val identificacion: String,
    val nombres: String,
    val email: String,
    val password: String,
    val rol: Int
)
