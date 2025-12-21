package com.digicolor.rahuldemo.domain.model

data class PortfolioSummary(
    val currentValue: Double,
    val totalInvestment: Double,
    val totalPnL: Double,
    val totalPnLPercentage: Double,
    val todayPnL: Double,
    val todayPnLPercentage: Double
)