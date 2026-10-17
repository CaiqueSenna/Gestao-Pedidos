package com.caiquesenna.gestaopedidos.domain.model

data class Produto(
    val id: Long = 0L,
    val nome: String,
    val descricao: String = "",
    val preco: Double,
    val estoque: Int = 0
)
