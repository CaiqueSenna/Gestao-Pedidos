package com.caiquesenna.gestaopedidos.data.repository

import com.caiquesenna.gestaopedidos.data.local.dao.PedidoDao
import com.caiquesenna.gestaopedidos.data.local.entity.PedidoEntity
import com.caiquesenna.gestaopedidos.data.mapper.toDomain
import com.caiquesenna.gestaopedidos.data.mapper.toEntity
import com.caiquesenna.gestaopedidos.domain.model.Pedido
import com.caiquesenna.gestaopedidos.domain.repository.PedidoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PedidoRepositoryImpl @Inject constructor(
    private val dao: PedidoDao
): PedidoRepository {

    override fun observarPedidos(): Flow<List<Pedido>> =
        dao.observarTodos().map { lista -> lista.map { it.toDomain() } }

    override fun contarPedidos(): Flow<Int> = dao.contar()

    override fun somarFaturamento(): Flow<Double> = dao.somarTotal()

    override suspend fun buscarPedido(id: Long): Pedido? =
        dao.buscarPorId(id)?.toDomain()

    override suspend fun criarPedido(pedido: Pedido): Long {
        val cabecalho = PedidoEntity(
            clienteId = pedido.cliente.id,
            data = pedido.data,
            total = pedido.total,
            observacao = pedido.observacao
        )
        return dao.inserirPedidoComItens(cabecalho, pedido.itens.map { it.toEntity(0L) })
    }

    override suspend fun excluirPedido(id: Long) = dao.excluir(id)

    override suspend fun contarPedidosDoCliente(clienteId: Long): Int =
        dao.contarPorCliente(clienteId)
}