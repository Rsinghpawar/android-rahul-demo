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

    val totalPnL: Double
        get() = (lastTradedPrice - avgPrice) * quantity
}
