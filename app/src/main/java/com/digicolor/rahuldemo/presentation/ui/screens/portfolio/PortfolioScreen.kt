package com.digicolor.rahuldemo.presentation.ui.screens.portfolio

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.presentation.theme.RahulDemoTheme
import com.digicolor.rahuldemo.presentation.ui.components.BodyText
import com.digicolor.rahuldemo.presentation.ui.components.SecondaryText
import com.digicolor.rahuldemo.presentation.ui.widgets.StockListItem

@Composable
fun PortfolioRoute(
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PortfolioScreen(
        state = uiState,
        onTabSelected = viewModel::onTabSelected,
        onToggleSummary = viewModel::toggleSummary,
        onRefresh = viewModel::onRefresh
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    state: PortfolioUiState,
    onTabSelected: (PortfolioTab) -> Unit = {},
    onToggleSummary: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    Scaffold(
        topBar = { PortfolioTopBar() },
        bottomBar = {
            PortfolioSummaryView(state = state, onToggle = onToggleSummary)
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                PortfolioTabs(
                    selectedTab = state.selectedTab,
                    onTabSelected = onTabSelected
                )

                AnimatedContent(
                    targetState = state.selectedTab,
                    transitionSpec = {
                        if (targetState.ordinal > initialState.ordinal) {
                            slideInHorizontally { it } + fadeIn() togetherWith
                                    slideOutHorizontally { -it } + fadeOut()
                        } else {
                            slideInHorizontally { -it } + fadeIn() togetherWith
                                    slideOutHorizontally { it } + fadeOut()
                        }.using(
                            SizeTransform(clip = false)
                        )
                    },
                    label = "TabSwitchAnimation",
                    modifier = Modifier.weight(1f)
                ) { targetTab ->
                    when (targetTab) {
                        PortfolioTab.POSITIONS -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                contentAlignment = Alignment.Center
                            ) {
                                BodyText(text = "No orders yet", color = Color.Gray)
                            }
                        }

                        PortfolioTab.HOLDINGS -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .then(
                                        if (state.error != null) Modifier.verticalScroll(rememberScrollState())
                                        else Modifier
                                    )
                            ) {
                                when {
                                    state.isLoading && !state.isRefreshing -> {
                                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                    }

                                    state.error != null -> {
                                        SecondaryText(
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
                isExpanded = true,
                selectedTab = PortfolioTab.HOLDINGS
            )
        )
    }
}