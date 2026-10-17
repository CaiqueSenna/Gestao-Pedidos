package com.caiquesenna.gestaopedidos.domain.model

data class Cliente(
    val id: Long = 0L,
    val nome: String,
    val email: String = "",
    val telefone: String = "",
    val documento: String = ""
)
