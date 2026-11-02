package com.caiquesenna.gestaopedidos.ui.cliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caiquesenna.gestaopedidos.domain.model.Cliente
import com.caiquesenna.gestaopedidos.domain.usecase.cliente.ExcluirClienteUseCase
import com.caiquesenna.gestaopedidos.domain.usecase.cliente.ObservarClienteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClienteListUiState(
    val carregando: Boolean = true,
    val clientes: List<Cliente> = emptyList()
)

@HiltViewModel
class ClienteListViewModel @Inject constructor(
    observarClientes: ObservarClienteUseCase,
    private val excluirCliente: ExcluirClienteUseCase
) : ViewModel() {

    val uiState: StateFlow<ClienteListUiState> = observarClientes()
        .map { ClienteListUiState(carregando = false, clientes = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ClienteListUiState())

    private val _mensagens = Channel<String>()
    val mensagens = _mensagens.receiveAsFlow()

    fun excluir(cliente: Cliente) {
        viewModelScope.launch {
            excluirCliente(cliente.id)
                .onSuccess { _mensagens.send("Cliente excluido") }
                .onFailure { _mensagens.send(it.message ?: "Não foi possivel excluir") }
        }
    }
}