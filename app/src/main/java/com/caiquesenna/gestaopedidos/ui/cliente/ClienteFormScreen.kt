package com.caiquesenna.gestaopedidos.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.copy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteFormScreen(
    onVoltar: () -> Unit,
    viewModel: ClienteFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ehEdicao = viewModel.clienteId > 0L

    LaunchedEffect(uiState.salvoComSucesso) {
        if (uiState.salvoComSucesso) onVoltar()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (ehEdicao) "Editar cliente" else "Novo cliente") },
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
            OutlinedTextField(
                value = uiState.nome,
                onValueChange = viewModel::onNomeChange,
                label = {Text("Nome *")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = {Text("E-mail *")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.telefone,
                onValueChange = viewModel::onTelefoneChange,
                label = {Text("Telefone *")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.documento,
                onValueChange = viewModel::onDocumentoChange,
                label = {Text("CPF / CNPJ")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            uiState.erro?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = viewModel::salvar,
                enabled = !uiState.salvando,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(if (uiState.salvando) "Salvando..." else "Salvar")
            }
        }
    }
}