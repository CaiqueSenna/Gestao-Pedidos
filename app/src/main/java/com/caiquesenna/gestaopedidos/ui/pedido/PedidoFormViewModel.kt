package com.caiquesenna.gestaopedidos.ui.pedido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caiquesenna.gestaopedidos.domain.model.Cliente
import com.caiquesenna.gestaopedidos.domain.model.ItemPedido
import com.caiquesenna.gestaopedidos.domain.model.Pedido
import com.caiquesenna.gestaopedidos.domain.model.Produto
import com.caiquesenna.gestaopedidos.domain.usecase.cliente.ObservarClienteUseCase
import com.caiquesenna.gestaopedidos.domain.usecase.pedido.CriarPedidoUseCase
import com.caiquesenna.gestaopedidos.domain.usecase.produto.ObservarProdutoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PedidoFormUiState(
    val clientes: List<Cliente> = emptyList(),
    val produtos: List<Produto> = emptyList(),
    val clienteSelecionado: Cliente? = null,
    val itens: List<ItemPedido> = emptyList(),
    val observacao: String = "",
    val salvando: Boolean = false,
    val erro: String? = null,
    val salvoComSucesso: Boolean = false
) {
    val total: Double get() = itens.sumOf { it.subtotal }
}

@HiltViewModel
class PedidoFormViewModel @Inject constructor(
    observarClientes: ObservarClienteUseCase,
    observarProdutos: ObservarProdutoUseCase,
    private val criarPedido: CriarPedidoUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(PedidoFormUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observarClientes().collect { lista ->
                _uiState.update { it.copy(clientes = lista) }
            }
        }
        viewModelScope.launch {
            observarProdutos().collect { lista ->
                _uiState.update { it.copy(produtos = lista) }
            }
        }
    }

    fun selecionarCliente(cliente: Cliente) =
        _uiState.update { it.copy(clienteSelecionado = cliente, erro = null) }

    fun onObservacaoChange(valor: String) =
        _uiState.update { it.copy(observacao = valor) }

    fun adicionarItem(produto: Produto, quantidade: Int) {
        if (quantidade <= 0) return
        _uiState.update { estado ->
            val existente = estado.itens.find { it.produtoId == produto.id }
            val novosItens = if (existente != null) {
                estado.itens.map {
                    if (it.produtoId == produto.id) {
                        it.copy(quantidade = it.quantidade + quantidade)
                    } else {
                        it
                    }
                }
            } else {
                estado.itens + ItemPedido (
                    produtoId = produto.id,
                    produtoNome = produto.nome,
                    quantidade = quantidade,
                    precoUnitario = produto.preco
                )
            }
            estado.copy(itens = novosItens, erro = null)
        }
    }

    fun removerItem(item: ItemPedido) =
        _uiState.update { it.copy(itens = it.itens - item) }

    fun salvar() {
        val estado = _uiState.value
    val cliente = estado.clienteSelecionado
    if (cliente == null){
        _uiState.update { it.copy(erro = "Selecione um cliente") }
        return
    }
    viewModelScope.launch {
        _uiState.update { it.copy(salvando = true, erro = null) }
        val pedido = Pedido(
            cliente = cliente,
            observacao = estado.observacao.trim(),
            itens = estado.itens
        )
        criarPedido(pedido)
            .onSuccess { _uiState.update { s -> s.copy(salvando = false, salvoComSucesso = true) } }
            .onFailure { e -> _uiState.update { s -> s.copy(salvando = false, erro = e.message) } }
    }
  }
}