package co.edu.cecar.smarbook.navegation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import co.edu.cecar.smarbook.data.local.SessionManager
import co.edu.cecar.smarbook.screen.PantallaDashboard
import co.edu.cecar.smarbook.screen.PantallaMenuPrincipal
import co.edu.cecar.smarbook.screen.PantallaNuevaContrasena
import co.edu.cecar.smarbook.screen.PantallaSolicitudRestablecimiento
import co.edu.cecar.smarbook.screen.Pantallalogin
import kotlinx.coroutines.launch

@Composable
fun AppNavegation() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val isLoggedIn by sessionManager.isLoggedIn().collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    if (isLoggedIn == null) {
        return
    }

    val backStack = rememberNavBackStack(if (isLoggedIn == true) MenuPrincipalRoute else LoginRoute)

    NavDisplay(
        backStack = backStack,

        onBack = {
            if (backStack.lastOrNull() == MenuPrincipalRoute) {
                // No vuelve al login
            } else {
                backStack.removeLastOrNull()
            }
        },

        entryProvider = entryProvider {
            entry<LoginRoute> {
                Pantallalogin(
                    navegarAPantallaDashboard = {
                        backStack.clear()
                        backStack.add(MenuPrincipalRoute)
                    },
                    navegarAPantallaSolicitudRestablecimiento = {
                        backStack.add(RestablecerContrsenaRoute)
                    }
                )
            }

            entry<MenuPrincipalRoute> {
                PantallaMenuPrincipal(
                    cerrarSesion = {
                        scope.launch {
                            sessionManager.clearSession()
                            backStack.clear()
                            backStack.add(LoginRoute)
                        }
                    }
                )
            }

            entry<RestablecerContrsenaRoute> {
                PantallaSolicitudRestablecimiento(
                    NavegarPantallaLogin = {
                        backStack.clear()
                        backStack.add(LoginRoute)
                    },
                    NavegarPantallaNuevaContrasena = {
                        backStack.add(NuevaContrasenaRoute)
                    }
                )
            }

            entry<NuevaContrasenaRoute> {
                PantallaNuevaContrasena(
                    NavegarPantallaLogin = {
                        backStack.clear()
                        backStack.add(LoginRoute)
                    },
                    navegarAPantallaSolicitudRestablecimiento = {
                        backStack.add(RestablecerContrsenaRoute)
                    }
                )
            }


        })
}






