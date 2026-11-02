package com.caiquesenna.gestaopedidos.ui.pedido

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
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.ReceiptLong
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.caiquesenna.gestaopedidos.domain.model.Pedido
import com.caiquesenna.gestaopedidos.ui.components.DialogoConfirmacao
import com.caiquesenna.gestaopedidos.ui.components.EstadoVazio
import com.caiquesenna.gestaopedidos.ui.util.CompartilharPdf
import com.caiquesenna.gestaopedidos.ui.util.formatarData
import com.caiquesenna.gestaopedidos.ui.util.formatarMoeda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoListScreen(
    onNovoPedido: () -> Unit,
    viewModel: PedidoListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var pedidoParaExcluir by remember { mutableStateOf<Pedido?>(null) }

    LaunchedEffect(Unit) {
        viewModel.evento.collect { evento ->
            when (evento) {
                is PedidoEvento.Mensagem -> snackbarHostState.showSnackbar(evento.texto)
                is PedidoEvento.AbrirPdf -> CompartilharPdf(context, evento.arquivo)
            }
        }
    }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Pedidos") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNovoPedido) {
                Icon(Icons.Default.Add, contentDescription = "Novo pedido")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                uiState.carregando -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                uiState.pedidos.isEmpty() -> EstadoVazio(
                    icone = Icons.Default.ReceiptLong,
                    titulo = "Nenhum pedido",
                    descricao = "Cadastre clientes e produtos, depois emita seus primeiro pedido."
                )
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = uiState.pedidos, key = {it.id}) { pedido ->
                        ItemPedidoCard(
                            pedido = pedido,
                            onGerarPdf = { viewModel.gerarPdfDoPedido(pedido)},
                            onExcluir = { pedidoParaExcluir = pedido}
                        )
                    }
                }
            }
        }
    }

    pedidoParaExcluir?.let { pedido ->
        DialogoConfirmacao(
            titulo = "Excluir pedido",
            mensagem = "Deseja excluir o pedido #${pedido.id}?",
            onConfirmar = {
                viewModel.excluir(pedido)
                pedidoParaExcluir = null
            },
            onCancelar = {pedidoParaExcluir = null}
        )
    }
}

@Composable
private fun ItemPedidoCard(
    pedido: Pedido,
    onGerarPdf: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pedido #${pedido.id} - ${pedido.cliente.nome}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = pedido.data.formatarData(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${pedido.itens.size} item(ns) - ${pedido.total.formatarMoeda()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onGerarPdf) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = "Gerar PDF")
            }
            IconButton(onClick = onExcluir) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir")
            }
        }
    }
}