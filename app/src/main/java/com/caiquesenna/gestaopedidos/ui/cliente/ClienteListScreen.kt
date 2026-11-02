package com.caiquesenna.gestaopedidos.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.caiquesenna.gestaopedidos.domain.model.Cliente
import com.caiquesenna.gestaopedidos.ui.components.DialogoConfirmacao
import com.caiquesenna.gestaopedidos.ui.components.EstadoVazio
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteListScreen (
    onNovoCliente: () -> Unit,
    onEditarCliente: (Long) -> Unit,
    viewModel: ClienteListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember{ SnackbarHostState() }
    var clienteParaExcluir by remember { mutableStateOf<Cliente?>(null) }

    LaunchedEffect(Unit) {
        viewModel.mensagens.collect { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Clientes") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNovoCliente) {
                Icon(Icons.Default.Add, contentDescription = "Novo Cliente")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)){
            when {
                uiState.carregando -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                uiState.clientes.isEmpty() -> EstadoVazio(
                    icone = Icons.Default.People,
                    titulo = "Nenhum cliente",
                    descricao = "Cadastre um cliente para poder emitir pedidos."
                )
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = uiState.clientes, key = {it.id}) { cliente ->
                        ItemCliente(
                            cliente = cliente,
                            onEditar = { onEditarCliente(cliente.id)},
                            onExcluir = { clienteParaExcluir = cliente }
                        )
                    }
                }
            }
        }
    }

    clienteParaExcluir?.let { cliente ->
        DialogoConfirmacao(
            titulo = "Excluir cliente",
            mensagem = "Deseja excluir \"${cliente.nome}\"?",
            onConfirmar = {
                viewModel.excluir(cliente)
                clienteParaExcluir = null
            },
            onCancelar = { clienteParaExcluir = null}
        )
    }
}

@Composable
private fun ItemCliente (
    cliente: Cliente,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(cliente.nome, style = MaterialTheme.typography.titleMedium)
                if (cliente.telefone.isNotBlank()) {
                    Text(cliente.telefone, style = MaterialTheme.typography.bodyMedium)
                }
                if (cliente.email.isNotBlank()) {
                    Text(
                        text = cliente.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onEditar) { Icon(Icons.Default.Edit, "Editar")}
            IconButton(onClick = onExcluir) { Icon(Icons.Default.Delete, "Excluir")}
        }
    }
}