package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.errores.ApiErrorResponse
import co.edu.cecar.smarbook.model.usuario.MensajeResponseUsua
import co.edu.cecar.smarbook.model.usuario.MensajeResponseUsuario
import co.edu.cecar.smarbook.model.usuario.UsuarioEditarRequest
import co.edu.cecar.smarbook.model.usuario.UsuarioRequest
import co.edu.cecar.smarbook.model.usuario.UsuarioResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class UsuarioRepository {
    private val client = HttpClientProvider.client

    private val BASE_URL_API =
        "https://api.smartbooks.cecar.cloud/api/Usuarios"

    // LISTAR USUARIOS
    suspend fun listarUsuarios(
        token: String
    ): Result<List<UsuarioResponse>> = runCatching {
        val response = client.get(BASE_URL_API) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status.isSuccess()) {
            response.body<List<UsuarioResponse>>()
        } else {
            throw Exception("Error ${response.status.value}: No se pudieron cargar los usuarios")
        }
    }

    private val jsonHandler = Json { 
        ignoreUnknownKeys = true 
        isLenient = true
    }

    suspend fun crearUsuario(
        token: String,
        registrar: UsuarioRequest
    ): Result<MensajeResponseUsua> = runCatching {
        val response = client.post(BASE_URL_API) {
            header(HttpHeaders.Authorization, "Bearer $token")
            header(HttpHeaders.Accept, "*/*")
            setBody(registrar)
        }

        val bodyText = response.bodyAsText()

        if (response.status.isSuccess()) {
            jsonHandler.decodeFromString<MensajeResponseUsua>(bodyText)
        } else {
            val mensaje = try {
                val errorObj = jsonHandler.decodeFromString<ApiErrorResponse>(bodyText)
                errorObj.detail ?:
                errorObj.mensaje ?: 
                errorObj.errors?.values?.flatten()?.joinToString("\n") ?:
                errorObj.title ?:
                "Error ${response.status.value}"
            } catch (e: Exception) {
                if (bodyText.isNotEmpty() && !bodyText.contains("<!DOCTYPE")) bodyText 
                else "Error ${response.status.value}: No se pudo completar el registro"
            }
            throw Exception(mensaje)
        }
    }

    suspend fun editarUsuario(
        token: String,
        identificacion: Int,
        usuario: UsuarioEditarRequest
    ): Result<MensajeResponseUsuario> = runCatching {
        val response = client.put("$BASE_URL_API/$identificacion") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(usuario)
        }

        val bodyText = response.bodyAsText()

        if (response.status.isSuccess()) {
            jsonHandler.decodeFromString<MensajeResponseUsuario>(bodyText)
        } else {
            val mensaje = try {
                val errorObj = jsonHandler.decodeFromString<ApiErrorResponse>(bodyText)
                errorObj.mensaje ?: 
                errorObj.errors?.values?.flatten()?.joinToString("\n") ?:
                errorObj.title ?:
                "Error ${response.status.value}"
            } catch (e: Exception) {
                if (bodyText.isNotEmpty() && !bodyText.contains("<!DOCTYPE")) bodyText 
                else "Error ${response.status.value}: No se pudo actualizar el usuario"
            }
            throw Exception(mensaje)
        }
    }

}