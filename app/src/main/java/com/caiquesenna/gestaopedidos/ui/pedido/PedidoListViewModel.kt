package com.caiquesenna.gestaopedidos.ui.pedido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caiquesenna.gestaopedidos.domain.model.Pedido
import com.caiquesenna.gestaopedidos.domain.usecase.pedido.ExcluirPedidoUseCase
import com.caiquesenna.gestaopedidos.domain.usecase.pedido.GerarPdfPedidoUseCase
import com.caiquesenna.gestaopedidos.domain.usecase.pedido.ObservarPedidoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class PedidoListUiState(
    val carregando: Boolean = true,
    val pedidos: List<Pedido> = emptyList()
)

sealed interface PedidoEvento{
    data class AbrirPdf(val arquivo: File) : PedidoEvento
    data class Mensagem(val texto: String) : PedidoEvento
}

@HiltViewModel
class PedidoListViewModel @Inject constructor(
    observarPedidos: ObservarPedidoUseCase,
    private val excluirPedido: ExcluirPedidoUseCase,
    private val gerarPdf: GerarPdfPedidoUseCase
) : ViewModel() {

    val uiState: StateFlow<PedidoListUiState> = observarPedidos()
        .map { PedidoListUiState(carregando = false, pedidos = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_0000), PedidoListUiState())

    private val _eventos = Channel<PedidoEvento>()
    val evento = _eventos.receiveAsFlow()

    fun excluir(pedido: Pedido) {
        viewModelScope.launch {
            excluirPedido(pedido.id)
                .onSuccess { _eventos.send(PedidoEvento.Mensagem("Pedido excluido")) }
                .onFailure { _eventos.send(PedidoEvento.Mensagem(it.message ?: "Erro ao excluir")) }
        }
    }

    fun gerarPdfDoPedido(pedido: Pedido) {
        viewModelScope.launch {
            gerarPdf(pedido.id)
                .onSuccess { arquivo -> _eventos.send(PedidoEvento.AbrirPdf(arquivo)) }
                .onFailure { _eventos.send(PedidoEvento.Mensagem("Falha ao gerar PDF")) }
        }
    }
}