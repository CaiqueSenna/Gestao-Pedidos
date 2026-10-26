package com.caiquesenna.gestaopedidos.domain.usecase.produto

import com.caiquesenna.gestaopedidos.domain.model.Produto
import com.caiquesenna.gestaopedidos.domain.repository.ProdutoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObservarProdutoUseCase @Inject constructor(
    private val repository: ProdutoRepository
) {
    operator fun invoke(): Flow<List<Produto>> = repository.observarProdutos()
}

class BuscarProdutoUseCase @Inject constructor(
    private val repository: ProdutoRepository
) {
    suspend operator fun invoke(id: Long): Produto? = repository.buscarProduto(id)
}

class SalvarProdutoUseCase @Inject constructor(
    private val repository: ProdutoRepository
) {
    suspend operator fun invoke(produto: Produto): Result<Long> {
        if (produto.nome.isBlank()) {
            return Result.failure(IllegalArgumentException("Infome o nome do produto"))
        }
        if (produto.preco <= 0.0) {
            return Result.failure(IllegalArgumentException("O preço deve ser maior que zero"))
        }
        if (produto.estoque < 0) {
            return Result.failure(IllegalArgumentException("O estoque não pode ser negativo"))
        }
        return runCatching { repository.salvarProduto(produto) }
    }
}

class ExcluirProdutoUseCase @Inject constructor(
    private val repository: ProdutoRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> =
        runCatching { repository.excluirProduto(id) }
}