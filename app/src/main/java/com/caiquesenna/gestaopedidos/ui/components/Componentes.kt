package com.caiquesenna.gestaopedidos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EstadoVazio (
    icone: ImageVector,
    titulo: String,
    descricao: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                modifier = modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(
                text = descricao,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DialogoConfirmacao (
    titulo: String,
    mensagem: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog (
        onDismissRequest = onCancelar,
        title = { Text(titulo)},
        text = { Text(mensagem)},
        confirmButton = {
            TextButton(onClick = onConfirmar) {Text("Excluir") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {Text("Cancelar") }
        }
    )
}