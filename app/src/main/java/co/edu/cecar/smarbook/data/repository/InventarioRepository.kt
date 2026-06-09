package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.errores.ApiErrorResponse
import co.edu.cecar.smarbook.model.inventario.InventarioResponse
import co.edu.cecar.smarbook.model.lote.LoteResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class InventarioRepository {

    private val client = HttpClientProvider.client

    private val BASE_URL_API =
        "https://api.smartbooks.cecar.cloud/api/Inventarios"

    private val jsonHandler = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // LISTAR INVENTARIO CON FILTRO POR LOTE
    suspend fun listarInventario(
        token: String,
        loteId: Int? = null
    ): Result<List<InventarioResponse>> = runCatching {
        val url = if (loteId != null) "$BASE_URL_API?lote=$loteId" else BASE_URL_API

        val response = client.get(url) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        val bodyText = response.bodyAsText()

        if (response.status.isSuccess()) {
            jsonHandler.decodeFromString<List<InventarioResponse>>(bodyText)
        } else {
            val mensaje = try {
                val errorObj = jsonHandler.decodeFromString<ApiErrorResponse>(bodyText)
                errorObj.detail ?: errorObj.mensaje ?: errorObj.title ?: "Error ${response.status.value}"
            } catch (e: Exception) {
                if (bodyText.isNotEmpty() && !bodyText.contains("<!DOCTYPE")) bodyText
                else "Error ${response.status.value}: No se pudo cargar el inventario"
            }
            throw Exception(mensaje)
        }
    }
}
