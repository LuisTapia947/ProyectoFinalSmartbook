package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.errores.ApiErrorResponse
import co.edu.cecar.smarbook.model.venta.VentaRequest
import co.edu.cecar.smarbook.model.venta.VentaResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class VentaRepository {
    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Ventas"
    private val jsonHandler = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun listarVentas(token: String, libroId: Int? = null): Result<List<VentaResponse>> = runCatching {
        val url = if (libroId != null) "$BASE_URL_API?libro=$libroId" else BASE_URL_API
        val response = client.get(url) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        if (response.status.isSuccess()) {
            response.body<List<VentaResponse>>()
        } else {
            throw Exception("Error ${response.status.value}: No se pudieron cargar las ventas")
        }
    }

    suspend fun registrarVenta(token: String, request: VentaRequest): Result<VentaResponse> = runCatching {
        val response = client.post(BASE_URL_API) {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(request)
        }
        val bodyText = response.bodyAsText()
        if (response.status.isSuccess()) {
            jsonHandler.decodeFromString<VentaResponse>(bodyText)
        } else {
            val mensaje = try {
                val errorObj = jsonHandler.decodeFromString<ApiErrorResponse>(bodyText)
                errorObj.detail ?: errorObj.mensaje ?: errorObj.title ?: "Error ${response.status.value}"
            } catch (e: Exception) {
                if (bodyText.isNotEmpty() && !bodyText.contains("<!DOCTYPE")) bodyText
                else "Error ${response.status.value}: No se pudo registrar la venta"
            }
            throw Exception(mensaje)
        }
    }
}
