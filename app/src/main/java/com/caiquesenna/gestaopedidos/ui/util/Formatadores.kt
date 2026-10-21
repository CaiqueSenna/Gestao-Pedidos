package com.caiquesenna.gestaopedidos.ui.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val LOCALE_BR: Locale = Locale.forLanguageTag("pt-BR")

private val formatoMoeda: NumberFormat = NumberFormat.getCurrencyInstance(LOCALE_BR)
private val formatoData = SimpleDateFormat("dd/MM/yyyy HH:mm", LOCALE_BR)

fun Double.formatarMoeda(): String = formatoMoeda.format(this)
fun Long.formatarData(): String = formatoData.format(Date(this))

//Aceita "12,50" ou "12.50" e devolve null se não for numero valido.
fun String.paraDoubleOuNulo(): Double? = replace(".","").replace(",",".").toDoubleOrNull()