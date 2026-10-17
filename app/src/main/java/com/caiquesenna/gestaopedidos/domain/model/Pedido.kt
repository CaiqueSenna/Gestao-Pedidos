package com.caiquesenna.gestaopedidos.domain.model


data class ItemPedido(
    val produtoId: Long,
    val produtoNome: String,
    val quantidade: Int,
    val precoUnitario: Double
) {
    val subtotal: Double get() = quantidade * precoUnitario
}

data class Pedido(
    val id: Long = 0L,
    val cliente: Cliente,
    val data: Long = System.currentTimeMillis(),
    val observacao: String = "",
    val itens: List<ItemPedido> = emptyList()
) {
    val total: Double get() = itens.sumOf {it.subtotal}
}
