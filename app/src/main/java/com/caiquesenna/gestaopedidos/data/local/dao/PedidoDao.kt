package com.caiquesenna.gestaopedidos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.caiquesenna.gestaopedidos.data.local.entity.ItemPedidoEntity
import com.caiquesenna.gestaopedidos.data.local.entity.PedidoCompleto
import com.caiquesenna.gestaopedidos.data.local.entity.PedidoEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PedidoDao {

    @Transaction
    @Query("SELECT * FROM pedidos ORDER By data DESC")
    abstract fun observarTodos(): Flow<List<PedidoCompleto>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :id")
    abstract suspend fun buscarPorId(id: Long): PedidoCompleto?

    @Query("SELECT COUNT(*) FROM pedidos")
    abstract fun contar(): Flow<Int>

    @Query("SELECT COALESCE(SUM(total), 0.0) FROM pedidos")
    abstract fun somarTotal(): Flow<Double>

    @Query("SELECT COUNT(*) FROM pedidos WHERE clienteId = :clienteId")
    abstract suspend fun contarPorCliente(clienteId: Long): Int

    @Insert
    abstract suspend fun inserirPedido(pedido: PedidoEntity): Long

    @Insert
    abstract suspend fun inserirItens(itens: List<ItemPedidoEntity>)

    @Query("DELETE FROM pedidos WHERE id = :id")
    abstract suspend fun excluir(id: Long)

    @Transaction
    open suspend fun inserirPedidoComItens(
        pedido: PedidoEntity,
        itens: List<ItemPedidoEntity>
    ): Long {
        val pedidoId = inserirPedido(pedido)
        inserirItens(itens.map { it.copy(pedidoId = pedidoId) })
        return pedidoId
    }
}