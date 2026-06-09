package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.errores.ApiErrorResponse
import co.edu.cecar.smarbook.model.cliente.ClienteEditarRequest
import co.edu.cecar.smarbook.model.cliente.ClienteRequest
import co.edu.cecar.smarbook.model.cliente.ClienteResponse
import co.edu.cecar.smarbook.model.cliente.MensajeResponse
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

class ClienteRepository {

    private val client = HttpClientProvider.client

    private val BASE_URL_API =
        "https://api.smartbooks.cecar.cloud/api/Clientes"

    private val jsonHandler = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // LISTAR CLIENTES
    suspend fun listarClientes(
        token: String
    ): Result<List<ClienteResponse>> = runCatching {
        val response = client.get(BASE_URL_API) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status.isSuccess()) {
            response.body<List<ClienteResponse>>()
        } else {
            throw Exception("Error ${response.status.value}: No se pudieron cargar los clientes")
        }
    }

    // CREAR CLIENTE
    suspend fun crearCliente(
        token: String,
        registrar: ClienteRequest
    ): Result<ClienteResponse> = runCatching {
        val response = client.post(BASE_URL_API) {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(registrar)
        }

        val bodyText = response.bodyAsText()

        if (response.status.isSuccess()) {
            jsonHandler.decodeFromString<ClienteResponse>(bodyText)
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
                else "Error ${response.status.value}: No se pudo registrar el cliente"
            }
            throw Exception(mensaje)
        }
    }

    suspend fun editarCliente(
        token: String,
        identificacion: String,
        cliente: ClienteEditarRequest
    ): Result<MensajeResponse> = runCatching {
        val response = client.put("$BASE_URL_API/$identificacion") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(cliente)
        }

        val bodyText = response.bodyAsText()

        if (response.status.isSuccess()) {
            jsonHandler.decodeFromString<MensajeResponse>(bodyText)
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
                else "Error ${response.status.value}: No se pudo actualizar el cliente"
            }
            throw Exception(mensaje)
        }
    }
}

