package com.digicolor.rahuldemo.presentation.ui.screens.portfolio

import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.domain.model.PortfolioSummary
import com.digicolor.rahuldemo.util.ErrorType

data class PortfolioUiState(
    val holdings: List<Holding> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: ErrorType? = null,
    val isExpanded: Boolean = false,
    val summary: PortfolioSummary? = null,
    val selectedTab: PortfolioTab = PortfolioTab.HOLDINGS
)

enum class PortfolioTab {
    POSITIONS, HOLDINGS
}

sealed class PortfolioAction {
    object Refresh : PortfolioAction()
    data class TabSelected(val tab: PortfolioTab) : PortfolioAction()
    object ToggleSummary : PortfolioAction()
    object ErrorConsumed : PortfolioAction()
}