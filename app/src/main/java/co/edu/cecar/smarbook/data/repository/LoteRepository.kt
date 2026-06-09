package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.lote.LoteRequest
import co.edu.cecar.smarbook.model.lote.LoteResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

class LoteRepository {

    private val client = HttpClientProvider.client

    private val BASE_URL_API =
        "https://api.smartbooks.cecar.cloud/api/Lotes"

    // LISTAR LOTES
    suspend fun listarLotes(
        token: String
    ): Result<List<LoteResponse>> = runCatching {

        client.get(BASE_URL_API) {

            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )

        }.also {

            check(it.status.isSuccess()) {
                "Error: ${it.status.value}"
            }

        }.body()
    }

    // CREAR LOTE
    suspend fun crearLote(
        token: String,
        request: LoteRequest
    ): Result<String> = runCatching {

        client.post(BASE_URL_API) {

            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )

            setBody(request)

        }.also {

            check(it.status.isSuccess()) {
                "Error: ${it.status.value}"
            }

        }.bodyAsText()
    }
}