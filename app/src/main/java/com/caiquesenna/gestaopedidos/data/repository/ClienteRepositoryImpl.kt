package com.caiquesenna.gestaopedidos.data.repository

import com.caiquesenna.gestaopedidos.data.local.dao.ClienteDao
import com.caiquesenna.gestaopedidos.data.mapper.toDomain
import com.caiquesenna.gestaopedidos.data.mapper.toEntity
import com.caiquesenna.gestaopedidos.domain.model.Cliente
import com.caiquesenna.gestaopedidos.domain.repository.ClienteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClienteRepositoryImpl @Inject constructor(
    private val dao: ClienteDao
): ClienteRepository {

    override fun observarClientes(): Flow<List<Cliente>> =
        dao.observarTodos().map { lista -> lista.map { it.toDomain() } }

    override fun contarClientes(): Flow<Int> = dao.contar()

    override suspend fun buscarCliente(id: Long): Cliente? =
        dao.buscarPorId(id)?.toDomain()

    override suspend fun salvarCliente(cliente: Cliente): Long =
        dao.salvar(cliente.toEntity())
    override suspend fun excluirCliente(id: Long) = dao.excluir(id)
}