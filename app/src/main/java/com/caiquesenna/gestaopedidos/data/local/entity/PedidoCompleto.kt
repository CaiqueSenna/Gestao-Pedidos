package com.caiquesenna.gestaopedidos.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PedidoCompleto(
    @Embedded val pedido: PedidoEntity,
    @Relation(parentColumn = "clienteId", entityColumn = "id")
    val cliente: ClienteEntity,
    @Relation(parentColumn = "id", entityColumn = "pedidoId")
    val itens: List<ItemPedidoEntity>
)
