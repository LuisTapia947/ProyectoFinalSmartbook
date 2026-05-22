package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.Login.LoginRequest
import co.edu.cecar.smarbook.model.Login.LoginResponse
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class LoginRepository {
    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Seguridad/iniciar-sesion"


    suspend fun login   (iniciar: LoginRequest): Result<LoginResponse> = runCatching {
        client.post(BASE_URL_API) {
            setBody(iniciar)
        }.also {
            check(it.status.isSuccess()) { "Error: ${it.status.value}" }
        }.body<LoginResponse>()
    }

}