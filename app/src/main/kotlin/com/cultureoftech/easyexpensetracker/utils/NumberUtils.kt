package com.cultureoftech.easyexpensetracker.utils

import java.text.DecimalFormat
import java.util.*

fun Double.toCurrency() : String {
    val format = DecimalFormat("0.00")
    return Currency.getInstance(Locale.getDefault()).symbol + format.format(this)
}
