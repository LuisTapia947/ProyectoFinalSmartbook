package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.ingresos.IngresoRequest
import co.edu.cecar.smarbook.model.ingresos.IngresoResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

class IngresoRepository {

    private val client =
        HttpClientProvider.client

    private val BASE_URL_API =
        "https://api.smartbooks.cecar.cloud/api/Ingresos"

    // LISTAR INGRESOS
    suspend fun listarIngresos(
        token: String
    ): Result<List<IngresoResponse>> = runCatching {

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

    // CREAR INGRESO
    suspend fun crearIngreso(

        token: String,

        request: IngresoRequest

    ): Result<Unit> = runCatching {

        client.post(BASE_URL_API) {

            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )

            setBody(request)
        }.also {

            check(
                it.status.value == 201 ||
                        it.status.isSuccess()
            ) {
                "Error: ${it.status.value}"
            }
        }
    }

    // OBTENER POR ID
    suspend fun obtenerIngresoPorId(

        token: String,

        id: Int

    ): Result<IngresoResponse> = runCatching {

        client.get("$BASE_URL_API/$id") {

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

    // LISTAR LOTES
    suspend fun listarLotesIngresos(
        token: String
    ): Result<List<Int>> = runCatching {

        client.get("$BASE_URL_API/lotes") {

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
}