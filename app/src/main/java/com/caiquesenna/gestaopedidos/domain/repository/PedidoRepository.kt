package com.caiquesenna.gestaopedidos.domain.repository

import com.caiquesenna.gestaopedidos.domain.model.Pedido
import kotlinx.coroutines.flow.Flow

interface PedidoRepository {
    fun observarPedidos(): Flow<List<Pedido>>
    fun contarPedidos(): Flow<Int>
    fun somarFaturamento(): Flow<Double>
    suspend fun buscarPedido(id: Long): Pedido?
    suspend fun criarPedido(pedido: Pedido): Long
    suspend fun excluirPedido(id: Long)
    suspend fun contarPedidosDoCliente(clienteId: Long): Int
}