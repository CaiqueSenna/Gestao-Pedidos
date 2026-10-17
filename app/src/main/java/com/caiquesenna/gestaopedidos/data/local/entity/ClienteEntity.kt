package com.caiquesenna.gestaopedidos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nome: String,
    val email: String,
    val telefone: String,
    val documento: String
)
