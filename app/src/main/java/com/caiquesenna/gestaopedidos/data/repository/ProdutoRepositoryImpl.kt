package com.caiquesenna.gestaopedidos.data.repository

import com.caiquesenna.gestaopedidos.data.local.dao.ProdutoDao
import com.caiquesenna.gestaopedidos.data.mapper.toDomain
import com.caiquesenna.gestaopedidos.data.mapper.toEntity
import com.caiquesenna.gestaopedidos.domain.model.Produto
import com.caiquesenna.gestaopedidos.domain.repository.ProdutoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdutoRepositoryImpl @Inject constructor(
    private val dao: ProdutoDao
) : ProdutoRepository {

    override fun observarProdutos(): Flow<List<Produto>> =
        dao.observarTodos().map{ lista -> lista.map {it.toDomain()}}

    override fun contarProdutos(): Flow<Int> = dao.contar()

    override suspend fun buscarProduto(id: Long): Produto? =
        dao.buscarPorId(id)?.toDomain()

    override suspend fun salvarProduto(produto: Produto): Long =
        dao.salvar(produto.toEntity())

    override suspend fun excluirProduto(id: Long) = dao.excluir(id)
}