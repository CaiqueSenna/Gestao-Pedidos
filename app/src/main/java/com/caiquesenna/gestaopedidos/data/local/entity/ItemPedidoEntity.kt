package com.caiquesenna.gestaopedidos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "itens_pedido",
    foreignKeys = [
        ForeignKey(
            entity = PedidoEntity::class,
            parentColumns = ["id"],
            childColumns = ["pedidoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("pedidoId")]
)
data class ItemPedidoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val pedidoId: Long,
    val produtoId: Long,
    val produtoNome: String,
    val quantidade: Int,
    val precoUnitario: Double
)
