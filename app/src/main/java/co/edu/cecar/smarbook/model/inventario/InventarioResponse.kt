package co.edu.cecar.smarbook.model.inventario

import kotlinx.serialization.Serializable

@Serializable
data class InventarioResponse(
    val nivelLibro: String,
    val nombreLibro: String,
    val edicionLibro: String,
    val tipoLibro: String,
    val cantidadIngresada: Int,
    val cantidadVendida: Int,
    val stockDisponible: Int,
    val lote: Int
)
