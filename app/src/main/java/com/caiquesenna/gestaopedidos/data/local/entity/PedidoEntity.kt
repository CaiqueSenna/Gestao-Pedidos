package com.caiquesenna.gestaopedidos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "pedidos",
    foreignKeys = [
        ForeignKey(
            entity = ClienteEntity::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("clienteId")]
)
data class PedidoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val clienteId: Long,
    val data: Long,
    val total: Double,
    val observacao: String
)
