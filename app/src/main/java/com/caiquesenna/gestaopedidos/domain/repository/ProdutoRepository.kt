package com.caiquesenna.gestaopedidos.domain.repository

import com.caiquesenna.gestaopedidos.domain.model.Produto
import kotlinx.coroutines.flow.Flow

interface ProdutoRepository {
    fun observarProdutos(): Flow<List<Produto>>
    fun contarProdutos(): Flow<Int>
    suspend fun buscarProduto(id: Long): Produto?
    suspend fun salvarProduto(produto: Produto): Long
    suspend fun excluirProduto(id: Long)
}