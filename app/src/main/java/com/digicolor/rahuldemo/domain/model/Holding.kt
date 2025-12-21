package com.digicolor.rahuldemo.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Holding(
    val symbol: String,
    val quantity: Int,
    val lastTradedPrice: Double,
    val avgPrice: Double,
    val closePrice: Double
) {
    val currentValue: Double
        get() = lastTradedPrice * quantity

    val totalInvestment: Double
        get() = avgPrice * quantity

    val totalPnL: Double
        get() = (lastTradedPrice - avgPrice) * quantity

    val todayPnL: Double
        get() = (lastTradedPrice - closePrice) * quantity

    val pnlPercentage: Double
        get() = if (avgPrice != 0.0) {
            ((lastTradedPrice - avgPrice) / avgPrice) * 100
        } else 0.0

    val isProfit: Boolean
        get() = totalPnL >= 0
}
