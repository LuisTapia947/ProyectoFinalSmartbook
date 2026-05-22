package co.edu.cecar.smarbook.model.Login

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val identificacion: String,
    val nombres: String,
    val email: String,
    val rol: String,
    val activo: Boolean

)
@Serializable
data class LoginResponse(
    val token: String,
    val usuario: Usuario
)