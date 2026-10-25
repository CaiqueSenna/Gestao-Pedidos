package com.caiquesenna.gestaopedidos.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.caiquesenna.gestaopedidos.data.local.entity.ClienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Query ("SELECT * FROM clientes ORDER BY nome COLLATE NOCASE ASC")
    fun observarTodos(): Flow<List<ClienteEntity>>

    @Query ("SELECT COUNT (*) FROM clientes")
    fun contar(): Flow<Int>

    @Query ("SELECT * FROM clientes WHERE id = :id")
    suspend fun buscarPorId(id: Long): ClienteEntity?

    @Upsert
    suspend fun salvar(cliente: ClienteEntity): Long

    @Query ("DELETE FROM clientes WHERE id = :id")
    suspend fun excluir(id: Long)

}