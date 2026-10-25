package com.caiquesenna.gestaopedidos.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.caiquesenna.gestaopedidos.data.local.entity.ProdutoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Query ("SELECT * FROM produtos ORDER BY nome COLLATE NOCASE ASC")
    fun observarTodos(): Flow<List<ProdutoEntity>>

    @Query ("SELECT COUNT (*) FROM produtos")
    fun contar(): Flow<Int>

    @Query ("SELECT * FROM produtos WHERE id = :id")
    suspend fun buscarPorId(id: Long): ProdutoEntity?

    @Upsert
    suspend fun salvar(produto: ProdutoEntity): Long

    @Query("DELETE FROM produtos WHERE id = :id")
    suspend fun excluir(id: Long)
}