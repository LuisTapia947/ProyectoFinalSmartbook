package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.Login.LoginRequest
import co.edu.cecar.smarbook.model.Login.LoginResponse
import co.edu.cecar.smarbook.model.Login.RestablecimientoRequest
import co.edu.cecar.smarbook.model.Login.RestablecimientoResponse
import co.edu.cecar.smarbook.model.Login.NuevaContrasenaRequest
import co.edu.cecar.smarbook.model.errores.ApiErrorResponse
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class LoginRepository {
    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Seguridad"

    private val jsonHandler = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun login(iniciar: LoginRequest): Result<LoginResponse> = runCatching {
        client.post("$BASE_URL_API/iniciar-sesion") {
            setBody(iniciar)
        }.also {
            check(it.status.isSuccess()) { "Error: ${it.status.value}" }
        }.body<LoginResponse>()
    }

    suspend fun solicitarRestablecimiento(request: RestablecimientoRequest): Result<RestablecimientoResponse> = runCatching {
        val response = client.post("$BASE_URL_API/solicitar-restablecimiento") {
            setBody(request)
        }

        val bodyText = response.bodyAsText()

        if (response.status.isSuccess()) {
            jsonHandler.decodeFromString<RestablecimientoResponse>(bodyText)
        } else {
            val mensaje = try {
                val errorObj = jsonHandler.decodeFromString<ApiErrorResponse>(bodyText)
                errorObj.mensaje ?: errorObj.detail ?: errorObj.title ?: "Error ${response.status.value}"
            } catch (e: Exception) {
                "Error al solicitar restablecimiento"
            }
            throw Exception(mensaje)
        }
    }

    suspend fun restablecerContrasena(request: NuevaContrasenaRequest): Result<RestablecimientoResponse> = runCatching {
        val response = client.post("$BASE_URL_API/restablecer-contrasena") {
            setBody(request)
        }

        val bodyText = response.bodyAsText()

        if (response.status.isSuccess()) {
            jsonHandler.decodeFromString<RestablecimientoResponse>(bodyText)
        } else {
            val mensaje = try {
                val errorObj = jsonHandler.decodeFromString<ApiErrorResponse>(bodyText)
                errorObj.mensaje ?: errorObj.detail ?: errorObj.title ?: "Error ${response.status.value}"
            } catch (e: Exception) {
                "Error al restablecer contraseña"
            }
            throw Exception(mensaje)
        }
    }
}
