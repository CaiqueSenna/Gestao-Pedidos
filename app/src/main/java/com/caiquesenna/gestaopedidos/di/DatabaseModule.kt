package com.caiquesenna.gestaopedidos.di

import android.content.Context
import androidx.room.Room
import com.caiquesenna.gestaopedidos.data.local.AppDatabase
import com.caiquesenna.gestaopedidos.data.local.dao.ClienteDao
import com.caiquesenna.gestaopedidos.data.local.dao.PedidoDao
import com.caiquesenna.gestaopedidos.data.local.dao.ProdutoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "gestao_pedidos.db"
        ).build()

    @Provides
    fun provideProdutoDao(db: AppDatabase): ProdutoDao = db.produtoDao()

    @Provides
    fun provideClienteDao(db: AppDatabase): ClienteDao = db.clienteDao()

    @Provides
    fun providePedidoDao(db: AppDatabase): PedidoDao = db.pedidoDao()
}