package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.dashboart.DashboardResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

class DashboadRepository {
    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Dashboard"


    suspend fun obtenerDashboard   (token: String ): Result<DashboardResponse> = runCatching {
        client.get(BASE_URL_API) {
            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )
        }.also {
            check(it.status.isSuccess()) { "Error: ${it.status.value}" }
        }.body<DashboardResponse>()
    }



}