package co.edu.cecar.smarbook.model.Login

import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val email: String,
    val password: String

)
