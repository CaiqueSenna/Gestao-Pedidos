package com.caiquesenna.gestaopedidos.ui.produto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caiquesenna.gestaopedidos.domain.model.Produto
import com.caiquesenna.gestaopedidos.domain.usecase.produto.ExcluirProdutoUseCase
import com.caiquesenna.gestaopedidos.domain.usecase.produto.ObservarProdutoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProdutoListUiState(
    val carregando: Boolean = true,
    val produtos: List<Produto> = emptyList()
)

@HiltViewModel
class ProdutoListViewModel @Inject constructor(
    observarProdutos: ObservarProdutoUseCase,
    private val excluirProduto: ExcluirProdutoUseCase
) : ViewModel() {

    val uiState: StateFlow<ProdutoListUiState> = observarProdutos()
        .map { ProdutoListUiState(carregando = false, produtos = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProdutoListUiState()
        )

    private val _mensagens = Channel<String>()
    val mensagens = _mensagens.receiveAsFlow()

    fun excluir(produto: Produto) {
        viewModelScope.launch {
            excluirProduto(produto.id)
                .onSuccess { _mensagens.send("Produto excluido") }
                .onFailure { _mensagens.send(it.message ?: "Não foi possivel excluir") }
        }
    }
}