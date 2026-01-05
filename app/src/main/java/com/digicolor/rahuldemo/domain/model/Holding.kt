package com.digicolor.rahuldemo.domain.model

data class Holding(
    val symbol: String,
    val quantity: Int,
    val lastTradedPrice: Double,
    val avgPrice: Double,
    val closePrice: Double,
    val totalPnL : Double
)
