package com.caiquesenna.gestaopedidos.domain.usecase.pedido

import com.caiquesenna.gestaopedidos.domain.model.Pedido
import com.caiquesenna.gestaopedidos.domain.pdf.PedidoPdfGenerator
import com.caiquesenna.gestaopedidos.domain.repository.PedidoRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class ObservarPedidoUseCase @Inject constructor(
    private val repository: PedidoRepository
) {
    operator fun invoke(): Flow<List<Pedido>> = repository.observarPedidos()
}

class CriarPedidoUseCase @Inject constructor(
    private val repository: PedidoRepository
) {
    suspend operator fun invoke(pedido: Pedido): Result<Long> {
        if (pedido.cliente.id == 0L) {
            return Result.failure(IllegalArgumentException("Selecione um cliente"))
        }
        if (pedido.itens.isEmpty()) {
            return Result.failure(IllegalArgumentException("Adicione ao menos um produto"))
        }
        if (pedido.itens.any { it.quantidade <= 0 }) {
            return Result.failure(IllegalArgumentException("Quantidade invalida em um dos itens"))
        }
        return runCatching { repository.criarPedido(pedido) }
    }
}

class ExcluirPedidoUseCase @Inject constructor(
    private val repository: PedidoRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> =
        runCatching { repository.excluirPedido(id) }
}

class GerarPdfPedidoUseCase @Inject constructor(
    private val repository: PedidoRepository,
    private val pdfGenerator: PedidoPdfGenerator
) {
    suspend operator fun invoke(pedidoId: Long): Result<File> {
        val pedido = repository.buscarPedido(pedidoId)
            ?: return Result.failure(IllegalArgumentException("Pedido nao encontrado"))
        return runCatching { pdfGenerator.gerar(pedido) }
    }
}