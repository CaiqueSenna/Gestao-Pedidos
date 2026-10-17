package com.caiquesenna.gestaopedidos.domain.repository

import com.caiquesenna.gestaopedidos.domain.model.Cliente
import kotlinx.coroutines.flow.Flow

interface ClienteRepository {
    fun observarClientes(): Flow<List<Cliente>>
    fun contarClientes(): Flow<Int>
    suspend fun buscarCliente(id: Long): Cliente?
    suspend fun salvarCliente(cliente: Cliente): Long
    suspend fun excluirCliente(id: Long)
}