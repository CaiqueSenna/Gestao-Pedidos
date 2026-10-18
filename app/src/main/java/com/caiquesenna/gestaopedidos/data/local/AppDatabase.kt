package com.caiquesenna.gestaopedidos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.caiquesenna.gestaopedidos.data.local.dao.ClienteDao
import com.caiquesenna.gestaopedidos.data.local.dao.PedidoDao
import com.caiquesenna.gestaopedidos.data.local.dao.ProdutoDao
import com.caiquesenna.gestaopedidos.data.local.entity.ClienteEntity
import com.caiquesenna.gestaopedidos.data.local.entity.ItemPedidoEntity
import com.caiquesenna.gestaopedidos.data.local.entity.PedidoEntity
import com.caiquesenna.gestaopedidos.data.local.entity.ProdutoEntity

@Database(
    entities = [
        ProdutoEntity::class,
        ClienteEntity::class,
        PedidoEntity::class,
        ItemPedidoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun produtoDao(): ProdutoDao
    abstract fun clienteDao(): ClienteDao
    abstract fun pedidoDao(): PedidoDao
}