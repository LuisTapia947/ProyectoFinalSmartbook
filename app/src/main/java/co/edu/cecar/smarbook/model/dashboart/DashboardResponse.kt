package co.edu.cecar.smarbook.model.dashboart

import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    val totalLibros: Int,
    val totalClientes: Int,
    val cantVentasMes: Int,
    val totalVentasMes: Double,
    val ventasHoy: List <ventaHoy>
)
@Serializable
data class ventaHoy(
    val numeroRecibo: String,
    val total: Double,
    val fecha: String
)
