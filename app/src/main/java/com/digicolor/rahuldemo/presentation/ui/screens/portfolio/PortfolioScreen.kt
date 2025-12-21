package com.digicolor.rahuldemo.presentation.ui.screens.portfolio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.presentation.theme.RahulDemoTheme
import com.digicolor.rahuldemo.presentation.ui.widgets.StockListItem

@Composable
fun PortfolioRoute(
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PortfolioScreen(
        state = uiState,
        onTabSelected = viewModel::onTabSelected,
        onToggleSummary = viewModel::toggleSummary
    )
}

@Composable
fun PortfolioScreen(
    state: PortfolioUiState,
    onTabSelected: (PortfolioTab) -> Unit = {},
    onToggleSummary: () -> Unit = {}
) {
    Scaffold(
        topBar = { PortfolioTopBar() },
        bottomBar = {
            PortfolioSummaryView(state = state, onToggle = onToggleSummary)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            PortfolioTabs(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    state.error != null -> {
                        Text(
                            text = state.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(
                                items = state.holdings,
                                key = { it.symbol },
                            ) { holding ->
                                StockListItem(holding)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PortfolioScreenPreview() {
    RahulDemoTheme {
        PortfolioScreen(
            state = PortfolioUiState(
                holdings = listOf(
                    Holding(
                        symbol = "ASHOKLEY",
                        quantity = 3,
                        lastTradedPrice = 119.10,
                        avgPrice = 114.80,
                        closePrice = 115.0
                    )
                ),
                isExpanded = true
            )
        )
    }
}