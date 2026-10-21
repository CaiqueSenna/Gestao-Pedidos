package com.caiquesenna.gestaopedidos.domain.usecase.cliente

import com.caiquesenna.gestaopedidos.domain.model.Cliente
import com.caiquesenna.gestaopedidos.domain.repository.ClienteRepository
import com.caiquesenna.gestaopedidos.domain.repository.PedidoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservarClienteUseCase @Inject constructor(
    private val repository: ClienteRepository
) {
    operator fun invoke(): Flow<List<Cliente>> = repository.observarClientes()
}

class BuscarClienteUseCase @Inject constructor(
    private val repository: ClienteRepository
) {
    suspend fun invoke(id: Long): Cliente? = repository.buscarCliente(id)
}

class SalvarClienteUseCase @Inject constructor(
    private val repository: ClienteRepository
) {
    suspend fun invoke(cliente: Cliente): Result<Long> {
        if (cliente.nome.isBlank()) {
            return Result.failure(IllegalArgumentException("Informe o nome do cliente"))
        }
        if (cliente.email.isBlank() && !cliente.email.contains("@")) {
            return Result.failure(IllegalArgumentException("E-mail invalido"))
        }
        return runCatching { repository.salvarCliente(cliente) }
    }
}

class ExcluirClienteUseCase @Inject constructor(
    private val clienteRepository: ClienteRepository,
    private val pedidoRepository: PedidoRepository
) {
    suspend fun invoke(id: Long): Result<Unit> {
        val qtdPedidos = pedidoRepository.contarPedidosDoCliente(id)
        if (qtdPedidos > 0) {
            return Result.failure(
                IllegalStateException("Este cliente possui $qtdPedidos pedido(s) e nao pode ser excluido")
            )
        }
        return runCatching { clienteRepository.excluirCliente(id) }
    }
}