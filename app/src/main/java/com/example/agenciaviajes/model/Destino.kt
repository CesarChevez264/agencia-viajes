package com.example.agenciaviajes.model

data class Destino(
    var key: String? = null,
    var nombre: String = "",
    var pais: String = "",
    var precio: Double = 0.0,
    var descripcion: String = "",
    var imagenPath: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "key" to key,
            "nombre" to nombre,
            "pais" to pais,
            "precio" to precio,
            "descripcion" to descripcion,
            "imagenPath" to imagenPath
        )
    }
}