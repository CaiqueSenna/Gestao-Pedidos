package com.caiquesenna.gestaopedidos.domain.usecase.dashboard

import com.caiquesenna.gestaopedidos.domain.repository.ClienteRepository
import com.caiquesenna.gestaopedidos.domain.repository.PedidoRepository
import com.caiquesenna.gestaopedidos.domain.repository.ProdutoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


data class ResumoDashboard (
    val totalProdutos: Int = 0,
    val totalClientes: Int = 0,
    val totalPedidos: Int =0,
    val faturamento: Double = 0.0
)


class ObservarResumoUseCase @Inject constructor(
    private val produtoRepository: ProdutoRepository,
    private val clienteRepository: ClienteRepository,
    private val pedidoRepository: PedidoRepository
) {
    operator fun invoke(): Flow<ResumoDashboard> = combine(
        produtoRepository.contarProdutos(),
        clienteRepository.contarClientes(),
        pedidoRepository.contarPedidos(),
        pedidoRepository.somarFaturamento()
    ) { produtos, clientes, pedidos, faturamento ->
        ResumoDashboard(produtos, clientes, pedidos, faturamento)
    }
}