package com.caiquesenna.gestaopedidos.di

import com.caiquesenna.gestaopedidos.data.pdf.PedidoPdfGeneratorImpl
import com.caiquesenna.gestaopedidos.data.repository.ClienteRepositoryImpl
import com.caiquesenna.gestaopedidos.data.repository.PedidoRepositoryImpl
import com.caiquesenna.gestaopedidos.data.repository.ProdutoRepositoryImpl
import com.caiquesenna.gestaopedidos.domain.pdf.PedidoPdfGenerator
import com.caiquesenna.gestaopedidos.domain.repository.ClienteRepository
import com.caiquesenna.gestaopedidos.domain.repository.PedidoRepository
import com.caiquesenna.gestaopedidos.domain.repository.ProdutoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProdutoRepository(impl: ProdutoRepositoryImpl): ProdutoRepository

    @Binds
    @Singleton
    abstract fun bindClienteRepository(impl: ClienteRepositoryImpl): ClienteRepository

    @Binds
    @Singleton
    abstract fun bindPedidoRepository(impl: PedidoRepositoryImpl): PedidoRepository

    @Binds
    @Singleton
    abstract fun bindPedidoPdfGenerator(impl: PedidoPdfGeneratorImpl): PedidoPdfGenerator
}