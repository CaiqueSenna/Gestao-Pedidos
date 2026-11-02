package com.caiquesenna.gestaopedidos.ui.pedido

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.caiquesenna.gestaopedidos.domain.model.Produto
import com.caiquesenna.gestaopedidos.ui.util.formatarMoeda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoFormScreen(
    onVoltar: () -> Unit,
    viewModel: PedidoFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var menuClienteAberto by remember { mutableStateOf(false) }
    var menuProdutoAberto by remember { mutableStateOf(false) }
    var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }
    var quantidade by remember { mutableStateOf("1") }

    LaunchedEffect(uiState.salvoComSucesso) {
        if (uiState.salvoComSucesso) onVoltar()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo pedido") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ----- Cliente -----
            Text("Cliente", style = MaterialTheme.typography.titleMedium)
            ExposedDropdownMenuBox(
                expanded = menuClienteAberto,
                onExpandedChange = { menuClienteAberto = it}
            ) {
                OutlinedTextField(
                    value = uiState.clienteSelecionado?.nome ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Selecione o cliente")},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuClienteAberto)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryEditable)
                )
                ExposedDropdownMenu(
                    expanded = menuClienteAberto,
                    onDismissRequest = { menuClienteAberto = false }
                ) {
                    uiState.clientes.forEach { cliente ->
                        DropdownMenuItem(
                            text = {Text(cliente.nome)},
                            onClick = {
                                viewModel.selecionarCliente(cliente)
                                menuClienteAberto = false
                            }
                        )
                    }
                }
            }
            HorizontalDivider()

            // ---- Adicionar Produto -----
            Text("Adicionar produto", style = MaterialTheme.typography.titleMedium)
            ExposedDropdownMenuBox(
                expanded = menuProdutoAberto,
                onExpandedChange = { menuProdutoAberto = it}
            ) {
                OutlinedTextField(
                    value = produtoSelecionado?.nome ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = {Text("Produto")},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuProdutoAberto)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryEditable)
                )
                ExposedDropdownMenu(
                    expanded = menuProdutoAberto,
                    onDismissRequest = { menuProdutoAberto = false}
                ) {
                    uiState.produtos.forEach { produto ->
                        DropdownMenuItem(
                            text = {Text("${produto.nome} - ${produto.preco.formatarMoeda()}")},
                            onClick = {
                                produtoSelecionado = produto
                                menuProdutoAberto = false
                            }
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { quantidade = it.filter { c -> c.isDigit() }},
                    label = {Text("Qtd")},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(110.dp)
                )
                OutlinedButton(
                    onClick = {
                        produtoSelecionado?.let { produto ->
                            viewModel.adicionarItem(produto, quantidade.toIntOrNull() ?: 0)
                            produtoSelecionado = null
                            quantidade = "1"
                        }
                    },
                    enabled = produtoSelecionado != null,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Adicionar ao pedido")
                }
            }
            HorizontalDivider()

            // ----- Itens ----
            Text("Itens (${uiState.itens.size})", style = MaterialTheme.typography.titleMedium)

            if (uiState.itens.isEmpty()) {
                Text(
                    text = "Nenhum item adicionado ainda.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                uiState.itens.forEach { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(vertical = 10.dp)) {
                                Text(item.produtoNome, style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    text = "${item.quantidade} x ${item.precoUnitario.formatarMoeda()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(item.subtotal.formatarMoeda())
                            IconButton(onClick = { viewModel.removerItem(item)}) {
                                Icon(Icons.Default.Close, contentDescription = "Remover")
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", style = MaterialTheme.typography.titleMedium)
                Text(uiState.total.formatarMoeda(), style = MaterialTheme.typography.titleMedium)
            }

            OutlinedTextField(
                value = uiState.observacao,
                onValueChange = viewModel::onObservacaoChange,
                label = {Text("Observacoes")},
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            uiState.erro?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = viewModel::salvar,
                enabled = !uiState.salvando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.salvando) "Salvando..." else "Finalizar pedido")
            }
        }
    }
}