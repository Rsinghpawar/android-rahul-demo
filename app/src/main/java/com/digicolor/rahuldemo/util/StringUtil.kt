package com.digicolor.rahuldemo.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.digicolor.rahuldemo.R


@Composable
fun Double.toRupeeString(
    decimalPlaces: Int = 2,
    showSymbol: Boolean = true
): String {
    val context = LocalContext.current
    val symbol = if (showSymbol) {
        stringResource(R.string.currency_symbol_rupee)
    } else {
        ""
    }
    return when {
        this.isNaN() || this.isInfinite() -> "${symbol} 0.00"
        else -> {
            val formatted = String.format("%.${decimalPlaces}f", this)
            if (symbol.isNotEmpty()) "$symbol $formatted" else formatted
        }
    }
}