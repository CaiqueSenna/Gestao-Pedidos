package com.caiquesenna.gestaopedidos.ui.navigation

object Rotas {
    const val DASHBOARD = "dashboard"

    const val PRODUTOS = "produtos"
    const val PRODUTO_FROM = "produto_form?produtoId={produtoId}"
    fun produtoForm(id: Long = 0L) = "produto_form?produtoId=$id"

    const val CLIENTES = "clientes"
    const val CLIENTE_FROM = "cliente_form?clienteId={clienteId}"
    fun clienteForm(id: Long = 0L) = "cliente_form?clienteId=$id"

    const val PEDIDOS = "pedidos"
    const val PEDIDO_FORM = "pedido_form"

    const val ARG_PRODUTO_ID = "produtoId"
    const val ARG_CLIENTE_ID = "clienteId"

}