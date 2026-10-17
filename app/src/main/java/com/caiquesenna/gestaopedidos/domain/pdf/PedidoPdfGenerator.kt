package com.caiquesenna.gestaopedidos.domain.pdf

import com.caiquesenna.gestaopedidos.domain.model.Pedido
import java.io.File

interface PedidoPdfGenerator {
    suspend fun gerar(pedido: Pedido): File
}