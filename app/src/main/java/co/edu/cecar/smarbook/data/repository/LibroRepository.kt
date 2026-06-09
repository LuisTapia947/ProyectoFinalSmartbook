package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.libro.LibroRequest
import co.edu.cecar.smarbook.model.libro.LibroResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import co.edu.cecar.smarbook.model.libro.LibroEditarRequest
import co.edu.cecar.smarbook.model.cliente.MensajeResponse
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

class LibroRepository {

    private val client = HttpClientProvider.client

    private val BASE_URL_API =
        "https://api.smartbooks.cecar.cloud/api/Libros"

    // LISTAR LIBROS
    suspend fun listarLibros(
        token: String
    ): Result<List<LibroResponse>> = runCatching {

        client.get(BASE_URL_API) {

            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )

        }.also {

            check(it.status.isSuccess()) {
                "Error: ${it.status.value}"
            }

        }.body<List<LibroResponse>>()
    }

    // CREAR LIBRO
    suspend fun crearLibro(
        token: String,
        registrar: LibroRequest
    ): Result<String> = runCatching {

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

        }.bodyAsText()
    }

    // EDITAR LIBRO
    suspend fun editarLibro(
        token: String,
        id: Int,
        libro: LibroEditarRequest
    ): Result<String> = runCatching {

        client.put("$BASE_URL_API/$id") {

            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )

            setBody(libro)

        }.also {

            check(it.status.isSuccess()) {
                "Error: ${it.status.value}"
            }

        }.bodyAsText()
    }
}