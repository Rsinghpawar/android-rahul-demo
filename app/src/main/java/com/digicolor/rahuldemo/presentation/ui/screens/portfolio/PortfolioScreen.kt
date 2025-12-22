package com.digicolor.rahuldemo.presentation.ui.screens.portfolio

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.digicolor.rahuldemo.R
import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.presentation.theme.RahulDemoTheme
import com.digicolor.rahuldemo.presentation.ui.components.BodyText
import com.digicolor.rahuldemo.presentation.ui.components.LottieLoader
import com.digicolor.rahuldemo.presentation.ui.components.SecondaryText
import com.digicolor.rahuldemo.presentation.ui.widgets.StockListItem
import com.digicolor.rahuldemo.util.ErrorType

@Composable
fun PortfolioRoute(
    viewModel: PortfolioViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ErrorSnackBar(snackbarHostState, uiState, viewModel)

    PortfolioScreen(
        state = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun ErrorSnackBar(
    snackBarHostState: SnackbarHostState,
    uiState: PortfolioUiState,
    viewModel: PortfolioViewModel
) {
    val noInternetMessage = stringResource(R.string.error_no_internet)
    val serverErrorMessage = stringResource(R.string.something_went_wrong_on_our_end)
    val unknownErrorMessage = stringResource(R.string.an_unknown_error_occurred)

    LaunchedEffect(uiState.error) {
        if (uiState.error != null && uiState.holdings.isNotEmpty()) {
            val message = when (uiState.error) {
                ErrorType.NO_INTERNET -> noInternetMessage
                ErrorType.SERVER_ERROR -> serverErrorMessage
                else -> unknownErrorMessage
            }
            snackBarHostState.showSnackbar(message)
            viewModel.onAction(PortfolioAction.ErrorConsumed)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    state: PortfolioUiState,
    onAction: (PortfolioAction) -> Unit = {}
) {
    Scaffold(
        topBar = { PortfolioTopBar() },
        bottomBar = {
            PortfolioSummaryView(
                summary = state.summary,
                isExpanded = state.isExpanded,
                onToggle = { onAction(PortfolioAction.ToggleSummary) })
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(PortfolioAction.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                PortfolioTabs(
                    selectedTab = state.selectedTab,
                    onTabSelected = { onAction(PortfolioAction.TabSelected(it)) }
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
                            NoOrdersPlaceholder()
                        }

                        PortfolioTab.HOLDINGS -> {
                            HoldingsView(state)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HoldingsView(state: PortfolioUiState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (state.error != null && state.holdings.isEmpty()) Modifier.verticalScroll(
                    rememberScrollState()
                )
                else Modifier
            )
    ) {
        when {
            state.isLoading && !state.isRefreshing -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }

            state.error != null && state.holdings.isEmpty() -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ErrorView(state.error)
                }
            }

            else -> {
                if (state.holdings.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            items = state.holdings,
                            key = { it.symbol },
                        ) { holding ->
                            StockListItem(holding)
                        }
                    }
                } else {
                    NoOrdersPlaceholder()
                }
            }
        }
    }
}

@Composable
fun NoOrdersPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieLoader(R.raw.empty_ghost)
        BodyText(text = stringResource(R.string.no_orders_yet))
    }
}

@Composable
fun ErrorView(error: ErrorType?) {
    if (error == ErrorType.NO_INTERNET) {
        LottieLoader(
            resId = R.raw.no_internet,
            modifier = Modifier.size(200.dp),
            iterations = 1
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    SecondaryText(
        text = when (error) {
            ErrorType.NO_INTERNET -> stringResource(R.string.error_no_internet)
            ErrorType.SERVER_ERROR -> stringResource(R.string.something_went_wrong_on_our_end)
            else ->  stringResource(R.string.an_unknown_error_occurred)
        },
    )
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
                        closePrice = 115.0,
                        totalPnL = 2.0
                    )
                ),
                isExpanded = true,
                selectedTab = PortfolioTab.HOLDINGS
            )
        )
    }
}
