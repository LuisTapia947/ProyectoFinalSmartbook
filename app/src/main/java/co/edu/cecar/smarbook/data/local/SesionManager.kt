package co.edu.cecar.smarbook.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(
    name = "session_prefs"
)

class SessionManager(
    private val context: Context
) {

    companion object {
        val KEY_TOKEN = stringPreferencesKey("auth_token")
        val KEY_ROL = stringPreferencesKey("user_rol")
        val KEY_LOGEADO = booleanPreferencesKey("logeado")
        val KEY_NOMBRE = stringPreferencesKey("nombre_usuario")
    }

    suspend fun saveSession(
        token: String,
        rol: String,
        nombre: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
            prefs[KEY_ROL] = rol
            prefs[KEY_NOMBRE] = nombre
            prefs[KEY_LOGEADO] = true
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_TOKEN]
        }
    }

    fun getRol(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_ROL]
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_LOGEADO] ?: false
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
    fun getNombre(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_NOMBRE]
        }
    }
}