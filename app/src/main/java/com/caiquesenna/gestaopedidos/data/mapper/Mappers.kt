package com.caiquesenna.gestaopedidos.data.mapper

import com.caiquesenna.gestaopedidos.data.local.entity.ClienteEntity
import com.caiquesenna.gestaopedidos.data.local.entity.ItemPedidoEntity
import com.caiquesenna.gestaopedidos.data.local.entity.PedidoCompleto
import com.caiquesenna.gestaopedidos.data.local.entity.ProdutoEntity
import com.caiquesenna.gestaopedidos.domain.model.Cliente
import com.caiquesenna.gestaopedidos.domain.model.ItemPedido
import com.caiquesenna.gestaopedidos.domain.model.Pedido
import com.caiquesenna.gestaopedidos.domain.model.Produto

fun ProdutoEntity.toDomain() = Produto (
    id = id, nome = nome, descricao = descricao, preco = preco, estoque = estoque
)

fun Produto.toEntity() = ProdutoEntity(
    id = id, nome = nome, descricao = descricao, preco = preco, estoque = estoque
)

fun ClienteEntity.toDomain() = Cliente(
    id = id, nome = nome, email = email, telefone = telefone, documento = documento
)

fun Cliente.toEntity() = ClienteEntity(
    id = id, nome = nome, email = email, telefone = telefone, documento = documento
)

fun ItemPedidoEntity.toDomain() = ItemPedido(
    produtoId = produtoId,
    produtoNome = produtoNome,
    quantidade = quantidade,
    precoUnitario = precoUnitario
)

fun ItemPedido.toEntity(pedidoId: Long) = ItemPedidoEntity(
    pedidoId = pedidoId,
    produtoId = produtoId,
    produtoNome = produtoNome,
    quantidade = quantidade,
    precoUnitario = precoUnitario
)

fun PedidoCompleto.toDomain() = Pedido(
    id = pedido.id,
    cliente = cliente.toDomain(),
    data = pedido.data,
    observacao = pedido.observacao,
    itens = itens.map { it.toDomain() }
)