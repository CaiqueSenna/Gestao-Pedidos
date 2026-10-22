package com.caiquesenna.gestaopedidos.data.pdf

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import com.caiquesenna.gestaopedidos.domain.model.Pedido
import com.caiquesenna.gestaopedidos.domain.pdf.PedidoPdfGenerator
import com.caiquesenna.gestaopedidos.ui.util.formatarData
import com.caiquesenna.gestaopedidos.ui.util.formatarMoeda
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class PedidoPdfGeneratorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PedidoPdfGenerator {
    private companion object {
        //Folha A4 em pontos (72 dpi)
        const val LARGURA = 595
        const val ALTURA = 842
        const val MARGEM = 40f
    }
    private val titulo = Paint().apply {
        textSize = 20f
        typeface = Typeface.create (Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
    }
    private val subtitulo = Paint().apply {
        textSize = 12f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
    }
    private val normal = Paint().apply {
        textSize = 11f
        isAntiAlias = true
    }
    private val linha = Paint().apply {
        strokeWidth = 1f
        color = android.graphics.Color.LTGRAY
    }
    override suspend fun gerar(pedido: Pedido): File = withContext(Dispatchers.IO) {
        val documento = PdfDocument()
        val info = PdfDocument.PageInfo.Builder(LARGURA, ALTURA, 1).create()
        val pagina = documento.startPage(info)
        desenhar(pagina.canvas, pedido)
        documento.finishPage(pagina)

        val pasta = File(context.cacheDir,"pedidos").apply { mkdirs() }
        val arquivo = File(pasta, "pedido_${pedido.id}.pdf")
        FileOutputStream(arquivo).use { saida -> documento.writeTo(saida) }
        documento.close()

        arquivo
    }

    private fun desenhar(canvas: Canvas, pedido: Pedido) {
        var y = 60f

        canvas.drawText("PEDIDO #${pedido.id}", MARGEM, y, titulo)
        y += 18f
        canvas.drawText("Emitido em ${pedido.data.formatarData()}", MARGEM, y, normal)

        y += 24f
        canvas.drawLine(MARGEM, y, LARGURA - MARGEM, y, linha)

        //---- Clientes -----
        y += 24f
        canvas.drawText("CLIENTE", MARGEM, y,subtitulo)
        y += 16f
        canvas.drawText(pedido.cliente.nome, MARGEM, y, normal)
        if (pedido.cliente.documento.isNotBlank()) {
            y += 14f
            canvas.drawText("Documento: ${pedido.cliente.documento}", MARGEM, y, normal)
        }
        if (pedido.cliente.telefone.isNotBlank()) {
            y += 14f
            canvas.drawText("Telefone: ${pedido.cliente.telefone}", MARGEM, y, normal)
        }
        if (pedido.cliente.email.isNotBlank()) {
            y += 14f
            canvas.drawText("Email: ${pedido.cliente.email}", MARGEM, y, normal)
        }

        //---- Cabecalho da Tabela -----
        y += 30f
        canvas.drawText("ITENS", MARGEM, y, subtitulo)
        y += 18f
        canvas.drawText("Produto", MARGEM, y, subtitulo)
        canvas.drawText("Qtd", 330f, y, subtitulo)
        canvas.drawText("Unit", 390f, y, subtitulo)
        canvas.drawText("Subtotal", 480f, y, subtitulo)
        y +=6f
        canvas.drawLine(MARGEM, y, LARGURA - MARGEM, y, linha)

        //---- Itens -----
        pedido.itens.forEach { item ->
            y += 18f
            val nome = if (item.produtoNome.length > 38) {
                item.produtoNome.take(35) + "..."
            } else {
                item.produtoNome
            }
            canvas.drawText(nome, MARGEM, y, normal)
            canvas.drawText(item.quantidade.toString(), 330f, y, normal)
            canvas.drawText(item.precoUnitario.formatarMoeda(), 390f, y, normal)
            canvas.drawText(item.subtotal.formatarMoeda(), 480f, y, normal)
        }

        //---- Total -----
        y += 12f
        canvas.drawLine(MARGEM, y, LARGURA - MARGEM, y, linha)
        y += 24f
        canvas.drawText("TOTAL", 390f, y, subtitulo)
        canvas.drawText(pedido.total.formatarMoeda(), 480f, y, subtitulo)

        //---- Observacao -----
        if (pedido.observacao.isNotBlank()) {
            y += 36f
            canvas.drawText("OBSERVACAO", MARGEM, y, subtitulo)
            y += 16f
            pedido.observacao.chunked(80).forEach { trecho ->
                canvas.drawText(trecho, MARGEM, y, normal)
                y += 14f
            }
        }
        canvas.drawText (
            "Documento gerado pelo app Gestao de Pedidos",
            MARGEM,
            (ALTURA - 40).toFloat(),
            normal
        )
    }
}