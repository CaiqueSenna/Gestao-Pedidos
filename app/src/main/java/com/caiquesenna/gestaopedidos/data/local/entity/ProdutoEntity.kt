package com.caiquesenna.gestaopedidos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class ProdutoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nome: String,
    val descricao: String,
    val preco: Double,
    val estoque: Int
)
