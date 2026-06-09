package co.edu.cecar.smarbook.model.errores

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val title: String? = null,
    val detail: String? = null,
    val errors: Map<String, List<String>>? = null,
    val mensaje: String? = null
)
