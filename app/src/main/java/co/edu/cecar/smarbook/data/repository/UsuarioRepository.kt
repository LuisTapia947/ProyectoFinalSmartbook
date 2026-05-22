package co.edu.cecar.smarbook.data.repository

import co.edu.cecar.smarbook.data.remote.HttpClientProvider
import co.edu.cecar.smarbook.model.Login.Usuario
import co.edu.cecar.smarbook.model.cliente.ClienteEditarRequest
import co.edu.cecar.smarbook.model.cliente.ClienteRequest
import co.edu.cecar.smarbook.model.cliente.ClienteResponse
import co.edu.cecar.smarbook.model.cliente.MensajeResponse
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
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

class UsuarioRepository {
    private val client = HttpClientProvider.client

    private val BASE_URL_API =
        "https://api.smartbooks.cecar.cloud/api/Usuarios"

    // LISTAR CLIENTES
    suspend fun listarUsuarios(
        token: String
    ): Result<List<UsuarioResponse>> = runCatching {

        client.get(BASE_URL_API) {

            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )
        }.also {

            check(it.status.isSuccess()) {
                "Error: ${it.status.value}"
            }

        }.body<List< UsuarioResponse>>()
    }
    // CREAR CLIENTE
    suspend fun crearUsuario(
        token: String,
        registrar: UsuarioRequest
    ): Result<UsuarioResponse> = runCatching {

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

        }.body< UsuarioResponse>()
    }
    suspend fun editarUsuario(
        token: String,
        identificacion: Int,
        usuario: UsuarioEditarRequest
    ): Result<MensajeResponseUsuario> = runCatching {

        client.put("$BASE_URL_API/$identificacion") {
            header(
                HttpHeaders.Authorization,
                "Bearer $token"
            )

            setBody(usuario)

        }.also {
            check(it.status.isSuccess()) {
                "Error: ${it.status.value}"
            }
        }.body<MensajeResponseUsuario>()
    }

}