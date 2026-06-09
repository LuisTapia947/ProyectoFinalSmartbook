package co.edu.cecar.smarbook.model.usuario

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioResponse(
    val id : Int,
    val identificacion: String,
    val nombres: String,
    val email: String,
    val rol: String,
    val activo: Boolean

)
@Serializable
data class MensajeResponseUsua(
    val mensaje: String
)