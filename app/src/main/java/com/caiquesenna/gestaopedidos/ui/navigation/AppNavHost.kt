package com.caiquesenna.gestaopedidos.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.caiquesenna.gestaopedidos.ui.navigation.itensMenu

private data class ItemMenu (
    val rota: String,
    val rotulo: String,
    val icone: ImageVector
)

private val itensMenu = listOf(
    ItemMenu(Rotas.DASHBOARD, "Inicio", Icons.Default.Dashboard),
    ItemMenu(Rotas.PRODUTOS, "Produtos", Icons.Default.Inventory2 ),
    ItemMenu(Rotas.CLIENTES,"Clientes", Icons.Default.People),
    ItemMenu(Rotas.PEDIDOS, "Pedidos", Icons.Default.ReceiptLong)
)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    val mostrarMenu = rotaAtual in itensMenu.map {it.rota}

    Scaffold(
        bottomBar = {
            if (mostrarMenu) {
                NavigationBar {
                    itensMenu.forEach { item ->
                        NavigationBarItem(
                            selected = rotaAtual == item.rota,
                            onClick = { navController.navegarParaAba (item.rota)},
                            icon = { Icon(item.icone, contentDescription = item.rotulo) },
                            label = { Text(item.rotulo) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost (
            navController = NavController,
            startDestination = Rotas.DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rotas.DASHBOARD) {
                DashboardScreen (
                    onIrParaProdutos = { navController.navegarParaAba(Rotas.PRODUTOS)},
                    onIrParaClientes = { navController.navegarParaAba(Rotas.CLIENTES)},
                    onIrParaPedidos = { navController.navegarParaAba (Rotas.PEDIDOS)}
                )
            }

            // ---- Produtos -----
            composable(Rotas.PRODUTOS) {
                ProdutoListScreen (
                    onNovoProduto = { navController.navigate(Rotas.produtoForm())},
                    onEditarProduto = { id -> navController.navigate(Rotas.produtoForm(id))}
                )
            }
            composable(
                route = Rotas.PRODUTO_FROM,
                arguments = listOf(
                    navArgument(Rotas.ARG_PRODUTO_ID) {
                        type = NavType.LongType
                        defaultValue = 0L
                    }
                )
            ) {
                ProdutoFormScreen (onVoltar = { navController.popBackStack() })
            }

            // ---- Clientes -----
            composable(Rotas.CLIENTES) {
                ClienteListScreen (
                    onNovoCliente = { navController.navigate(Rotas.clienteForm())},
                    onEditarCliente = { id -> navController.navigate(Rotas.clienteForm(id))}
                )
            }
            composable (
                route = Rotas.CLIENTE_FROM,
                arguments = listOf(
                    navArgument(Rotas.ARG_CLIENTE_ID){
                        type = NavType.LongType
                        defaultValue = 0L
                    }
                )
            ) {
                ClienteFormScreen (onVoltar = { navController.popBackStack()})
            }

            // ---- Pedidos -----]
            composable(Rotas.PEDIDOS) {
                PedidoListScreen (
                    onNovoPedido = { navController.navigate(Rotas.PEDIDO_FORM)}
                )
            }
            composable(Rotas.PEDIDO_FORM) {
                PedidoFormScreen (onVOltar = { navController.popBackStack()})
            }
        }
    }
}

// Evitar empilhar copias da mesma aba ao trocar de menu
private fun NavHostController.navegarParaAba(rota: String) {
    navigate(rota) {
        popUpTo(graph.findStartDestination().id) {saveState = true}
        launchSingleTop = true
        restoreState = true
    }
}