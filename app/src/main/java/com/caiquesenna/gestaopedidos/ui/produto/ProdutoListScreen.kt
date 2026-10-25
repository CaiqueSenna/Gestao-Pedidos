package com.caiquesenna.gestaopedidos.ui.produto

import android.R.attr.onClick
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.caiquesenna.gestaopedidos.domain.model.Produto
import com.caiquesenna.gestaopedidos.ui.components.DialogoConfirmacao
import com.caiquesenna.gestaopedidos.ui.components.EstadoVazio
import com.caiquesenna.gestaopedidos.ui.util.formatarMoeda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutoListScreen(
    onNovoProduto: () -> Unit,
    onEditarProduto: (Long) -> Unit,
    viewModel: ProdutoListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var produtoParaExcluir by remember { mutableStateOf<Produto?>(null) }

    LaunchedEffect(Unit) {
        viewModel.mensagens.collect { mensagem ->
            snackbarHostState.showSnackbar(mensagem)
        }
    }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Produtos") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton = (onClick = onNovoProduto) {
                Icon(Icons.Default.Add, contentDescription = "Novo produto")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                uiState.carregando -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                uiState.produtos.isEmpty() -> {
                    EstadoVazio(
                        icone = Icons.Default.Inventory2,
                        titulo = "Nenhum produto",
                        descricao = "Toque no botao + para cadastrar seu primeiro produto."
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items = uiState.produtos, key = { it.id}) { produto ->
                            ItemProduto (
                                produto = produto,
                                onEditar = { onEditarProduto(produto.id)},
                                onExcluir = { produtoParaExcluir = produto}
                            )
                        }
                    }
                }
            }
        }
    }
    produtoParaExcluir?.let { produto ->
        DialogoConfirmacao(
            titulo = "Excluir produto",
            mensagem = "Deseja excluir \"${produto.nome}\"?",
            onConfirmar = {
                viewModel.excluir(produto)
                produtoParaExcluir = null
            },
            onCancelar = { produtoParaExcluir = null}
        )
    }
}

@Composable
private fun ItemProduto (
    produto: Produto,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(produto.nome, style = MaterialTheme.typography.titleMedium)
                if (produto.descricao.isNotBlank()) {
                    Text(
                        text = produto.descricao,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${produto.preco.formatarMoeda()} - estoque: ${produto.estoque}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onEditar) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onExcluir) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir")
            }
        }
    }
}