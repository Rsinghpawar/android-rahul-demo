package com.digicolor.rahuldemo.domain.usecase

import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.domain.model.PortfolioSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculatePortfolioSummaryUseCase @Inject constructor() {

    suspend operator fun invoke(holdings: List<Holding>): PortfolioSummary = withContext(Dispatchers.Default) {
        val currentValue = holdings.sumOf { it.lastTradedPrice * it.quantity }
        val totalInvestment = holdings.sumOf { it.avgPrice * it.quantity }
        val totalPnL = currentValue - totalInvestment
        val totalPnLPercentage = if (totalInvestment != 0.0) {
            (totalPnL / totalInvestment) * 100
        } else {
            0.0
        }

        val todayPnL = holdings.sumOf { (it.lastTradedPrice - it.closePrice) * it.quantity }
        val yesterdayValue = holdings.sumOf { it.closePrice * it.quantity }
        val todayPnLPercentage = if (yesterdayValue != 0.0) {
            (todayPnL / yesterdayValue) * 100
        } else {
            0.0
        }

        PortfolioSummary(
            currentValue = currentValue,
            totalInvestment = totalInvestment,
            totalPnL = totalPnL,
            totalPnLPercentage = totalPnLPercentage,
            todayPnL = todayPnL,
            todayPnLPercentage = todayPnLPercentage
        )
    }
}