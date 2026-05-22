package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
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
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

class ClienteRepository {

    private val client = HttpClientProvider.client

    private val BASE_URL_API =
        "https://api.smartbooks.cecar.cloud/api/Clientes"

    // LISTAR CLIENTES
    suspend fun listarClientes(
        token: String
    ): Result<List<ClienteResponse>> = runCatching {

        client.get(BASE_URL_API) {

            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )
        }.also {

            check(it.status.isSuccess()) {
                "Error: ${it.status.value}"
            }

        }.body<List<ClienteResponse>>()
    }

    // CREAR CLIENTE
    suspend fun crearCliente(
        token: String,
        registrar: ClienteRequest
    ): Result<ClienteResponse> = runCatching {

        client.post(BASE_URL_API) {

            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )

            setBody(registrar)
        }.also {

            check(it.status.isSuccess()) {
                "Error: ${it.status.value}"
            }

        }.body<ClienteResponse>()
    }

    suspend fun editarCliente(
        token: String,
        identificacion: String,
        cliente: ClienteEditarRequest
    ): Result<MensajeResponse> = runCatching {

        HttpClientProvider.client.put("$BASE_URL_API/$identificacion") {
            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )

            setBody(cliente)

        }.also {
            check(it.status.isSuccess()) {
                "Error: ${it.status.value}"
            }
        }.body<MensajeResponse>()
    }
}

