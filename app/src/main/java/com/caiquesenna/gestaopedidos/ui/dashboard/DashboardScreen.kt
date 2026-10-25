package com.caiquesenna.gestaopedidos.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen (
    onIrParaProdutos: () -> Unit,
    onIrParaClientes: () -> Unit,
    onIrParaPedidos: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val resumo by viewModel.resumo.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Gestao de Pedidos") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Resumo geral", style = MaterialTheme.typography.titleMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CardResumo(
                    titulo = "Produtos",
                    valor = resumo.totalProdutos.toString(),
                    icone = Icons.Default.Inventory2,
                    cor = MaterialTheme.colorScheme.primary,
                    onClick = onIrParaProdutos,
                    modifier = Modifier.weight(1f)
                )
                CardResumo(
                    titulo = "Clientes",
                    valor = resumo.totalClientes.toString(),
                    icone = Icons.Default.People,
                    cor = MaterialTheme.colorScheme.tertiary,
                    onClick = onIrParaClientes,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CardResumo(
                    titulo = "Pedidos",
                    valor = resumo.totalPedidos.toString(),
                    icone = Icons.Default.ReceiptLong,
                    cor = MaterialTheme.colorScheme.secondary,
                    onClick = onIrParaPedidos,
                    modifier = Modifier.weight(1f)
                )
                CardResumo(
                    titulo = "Faturamento",
                    valor = resumo.faturamento.toString(),
                    icone = Icons.Default.AttachMoney,
                    cor = MaterialTheme.colorScheme.primary,
                    onClick = onIrParaPedidos,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CardResumo(
    titulo: String,
    valor: String,
    icone: ImageVector,
    cor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = cor,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}