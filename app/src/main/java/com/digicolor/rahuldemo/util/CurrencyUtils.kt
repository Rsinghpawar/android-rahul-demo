package com.digicolor.rahuldemo.util

object CurrencyUtils {
    fun formatCurrency(
        value: Double,
        symbol: String,
        decimalPlaces: Int = 2
    ): String {
        return when {
            value.isNaN() || value.isInfinite() -> "$symbol 0.00"
            else -> {
                val formatted = String.format("%.${decimalPlaces}f", value)
                "$symbol $formatted"
            }
        }
    }
    fun formatFinancialValueIndian(
        value: Double,
        symbol: String,
        decimalPlaces: Int = 2
    ): String {
        return when {
            value.isNaN() || value.isInfinite() -> "$symbol 0.00"
            value < 0 -> {
                val absValue = kotlin.math.abs(value)
                val formatted = formatIndianNumber(absValue, decimalPlaces)
                "-$symbol $formatted"
            }
            else -> {
                val formatted = formatIndianNumber(value, decimalPlaces)
                "$symbol $formatted"
            }
        }
    }

    private fun formatIndianNumber(value: Double, decimalPlaces: Int): String {
        val formatted = String.format("%.${decimalPlaces}f", value)
        val parts = formatted.split(".")
        val integerPart = parts[0]
        val decimalPart = if (parts.size > 1) ".${parts[1]}" else ""

        if (integerPart.length <= 3) return formatted

        val lastThree = integerPart.takeLast(3)
        val remaining = integerPart.dropLast(3)

        val formattedRemaining = remaining.reversed()
            .chunked(2)
            .joinToString(",")
            .reversed()

        return "$formattedRemaining,$lastThree$decimalPart"
    }
}