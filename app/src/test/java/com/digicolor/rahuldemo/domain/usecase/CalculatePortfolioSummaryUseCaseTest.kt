package com.digicolor.rahuldemo.domain.usecase

import com.digicolor.rahuldemo.domain.model.Holding
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatePortfolioSummaryUseCaseTest {

    private lateinit var calculatePortfolioSummaryUseCase: CalculatePortfolioSummaryUseCase

    @Before
    fun setUp() {
        calculatePortfolioSummaryUseCase = CalculatePortfolioSummaryUseCase()
    }

    @Test
    fun `invoke with list of holdings should return correct summary`() = runTest {
        // Given
        val holdings = listOf(
            Holding(
                symbol = "MAHABANK",
                quantity = 10,
                lastTradedPrice = 40.0,
                avgPrice = 35.0,
                closePrice = 38.0
            ),
            Holding(
                symbol = "ICICI",
                quantity = 5,
                lastTradedPrice = 120.0,
                avgPrice = 110.0,
                closePrice = 115.0
            )
        )
        // Current Value: (40 * 10) + (120 * 5) = 400 + 600 = 1000
        // Total Investment: (35 * 10) + (110 * 5) = 350 + 550 = 900
        // Total PnL: 1000 - 900 = 100
        // Total PnL %: (100 / 900) * 100 = 11.11...
        // Today's PnL: ((40 - 38) * 10) + ((120 - 115) * 5) = (2 * 10) + (5 * 5) = 20 + 25 = 45
        // Yesterday Value: (38 * 10) + (115 * 5) = 380 + 575 = 955
        // Today's PnL %: (45 / 955) * 100 = 4.712...

        // When
        val result = calculatePortfolioSummaryUseCase(holdings)

        // Then
        assertEquals(1000.0, result.currentValue, 0.001)
        assertEquals(900.0, result.totalInvestment, 0.001)
        assertEquals(100.0, result.totalPnL, 0.001)
        assertEquals(11.111, result.totalPnLPercentage, 0.001)
        assertEquals(45.0, result.todayPnL, 0.001)
        assertEquals(4.712, result.todayPnLPercentage, 0.001)
    }

    @Test
    fun `invoke with empty list of holdings should return summary with zeros`() = runTest {
        // Given
        val holdings = emptyList<Holding>()

        // When
        val result = calculatePortfolioSummaryUseCase(holdings)

        // Then
        assertEquals(0.0, result.currentValue, 0.0)
        assertEquals(0.0, result.totalInvestment, 0.0)
        assertEquals(0.0, result.totalPnL, 0.0)
        assertEquals(0.0, result.totalPnLPercentage, 0.0)
        assertEquals(0.0, result.todayPnL, 0.0)
        assertEquals(0.0, result.todayPnLPercentage, 0.0)
    }

    @Test
    fun `invoke with holdings having zero investment should not crash and return zero percentage`() = runTest {
        // Given
        val holdings = listOf(
            Holding(
                symbol = "FREE_STOCK",
                quantity = 10,
                lastTradedPrice = 10.0,
                avgPrice = 0.0,
                closePrice = 10.0
            )
        )

        // When
        val result = calculatePortfolioSummaryUseCase(holdings)

        // Then
        assertEquals(100.0, result.currentValue, 0.0)
        assertEquals(0.0, result.totalInvestment, 0.0)
        assertEquals(100.0, result.totalPnL, 0.0)
        assertEquals(0.0, result.totalPnLPercentage, 0.0) // Handles division by zero
    }
}
