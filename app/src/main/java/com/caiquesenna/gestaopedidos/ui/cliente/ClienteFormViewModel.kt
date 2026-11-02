package com.caiquesenna.gestaopedidos.ui.cliente

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caiquesenna.gestaopedidos.domain.model.Cliente
import com.caiquesenna.gestaopedidos.domain.usecase.cliente.BuscarClienteUseCase
import com.caiquesenna.gestaopedidos.domain.usecase.cliente.SalvarClienteUseCase
import com.caiquesenna.gestaopedidos.ui.navigation.Rotas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClienteFormUiState(
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val documento: String = "",
    val salvando: Boolean = false,
    val erro: String? = null,
    val salvoComSucesso: Boolean = false
)

@HiltViewModel
class ClienteFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val buscarCliente: BuscarClienteUseCase,
    private val salvarCliente: SalvarClienteUseCase
) : ViewModel() {

    val clienteId: Long = savedStateHandle.get<Long>(Rotas.ARG_CLIENTE_ID) ?: 0L

    private val _uiState = MutableStateFlow(ClienteFormUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (clienteId > 0L) {
            viewModelScope.launch {
                buscarCliente(clienteId)?.let { cliente ->
                    _uiState.update {
                        it.copy(
                            nome = cliente.nome,
                            email = cliente.email,
                            telefone = cliente.telefone,
                            documento = cliente.documento
                        )
                    }
                }
            }
        }
    }

    fun onNomeChange(v: String) = _uiState.update { it.copy(nome = v, erro = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, erro = null) }
    fun onTelefoneChange(v: String) = _uiState.update { it.copy(telefone = v, erro = null) }
    fun onDocumentoChange(v: String) = _uiState.update { it.copy(documento = v, erro = null) }

    fun salvar(){
        val estado = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true, erro = null) }
            val cliente = Cliente(
                id = clienteId,
                nome = estado.nome.trim(),
                email = estado.email.trim(),
                telefone = estado.telefone.trim(),
                documento = estado.documento.trim()
            )
            salvarCliente(cliente)
                .onSuccess { _uiState.update { s -> s.copy(salvando = false, salvoComSucesso = true) } }
                .onFailure { e -> _uiState.update { s -> s.copy(salvando = false, erro = e.message) } }
        }
    }
}