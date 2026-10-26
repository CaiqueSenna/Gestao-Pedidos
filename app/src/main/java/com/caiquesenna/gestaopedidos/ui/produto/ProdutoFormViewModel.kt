package com.caiquesenna.gestaopedidos.ui.produto

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caiquesenna.gestaopedidos.domain.model.Produto
import com.caiquesenna.gestaopedidos.domain.usecase.produto.BuscarProdutoUseCase
import com.caiquesenna.gestaopedidos.domain.usecase.produto.SalvarProdutoUseCase
import com.caiquesenna.gestaopedidos.ui.navigation.Rotas
import com.caiquesenna.gestaopedidos.ui.util.paraDoubleOuNulo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProdutoFormUiState(
    val nome: String = "",
    val descricao: String = "",
    val preco: String = "",
    val estoque: String = "",
    val salvando: Boolean = false,
    val erro: String? = null,
    val salvoComSucesso: Boolean = false
)

@HiltViewModel
class ProdutoFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val buscarProduto: BuscarProdutoUseCase,
    private val salvarProduto: SalvarProdutoUseCase
) : ViewModel() {
    val produtoId: Long = savedStateHandle.get<Long>(Rotas.ARG_PRODUTO_ID) ?: 0L
    private val _uiState = MutableStateFlow(ProdutoFormUiState())
            val uiState = _uiState.asStateFlow()

    init {
        if (produtoId > 0L) carregarProduto()
    }
    private fun carregarProduto(){
        viewModelScope.launch {
            buscarProduto(produtoId)?.let { produto ->
                _uiState.update {
                    it.copy(
                        nome = produto.nome,
                        descricao = produto.descricao,
                        preco = produto.preco.toString().replace(".", ","),
                        estoque = produto.estoque.toString()
                    )
                }
            }
        }
    }

    fun onNomeChange(valor: String) = _uiState.update { it.copy(nome = valor, erro = null) }
    fun onDescricaoChange(valor: String) = _uiState.update { it.copy(descricao = valor) }
    fun onPrecoChange(valor: String) = _uiState.update { it.copy(preco = valor, erro = null) }
    fun onEstoqueChange(valor: String) = _uiState.update { it.copy(estoque = valor, erro = null) }

    fun salvar() {
        val estado = _uiState.value
        val preco = estado.preco.paraDoubleOuNulo()
        if (preco == null) {
            _uiState.update { it.copy(erro = "Preco Invalido") }
            return
        }
        val estoque = estado.estoque.toIntOrNull() ?: 0

        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true, erro = null) }
            val produto = Produto(
                id = produtoId,
                nome = estado.nome.trim(),
                descricao = estado.descricao.trim(),
                preco = preco,
                estoque = estoque
            )
            salvarProduto(produto)
                .onSuccess { _uiState.update { s -> s.copy(salvando = false, salvoComSucesso = true) } }
                .onFailure { erro ->
                    _uiState.update { s -> s.copy(salvando = false, erro = erro.message) }
                }
        }
    }
}