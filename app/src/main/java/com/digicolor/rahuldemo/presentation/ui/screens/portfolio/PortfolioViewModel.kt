package com.digicolor.rahuldemo.presentation.ui.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digicolor.rahuldemo.domain.usecase.CalculatePortfolioSummaryUseCase
import com.digicolor.rahuldemo.domain.usecase.GetUserHoldingsUseCase
import com.digicolor.rahuldemo.util.ErrorType
import com.digicolor.rahuldemo.util.NetworkMonitor
import com.digicolor.rahuldemo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getUserHoldingsUseCase: GetUserHoldingsUseCase,
    private val calculatePortfolioSummaryUseCase: CalculatePortfolioSummaryUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(PortfolioUiState())
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()
        .onStart { loadHoldings() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PortfolioUiState(isLoading = true)
        )

    private var loadJob: Job? = null

    init {
        monitorNetwork()
    }

    private fun monitorNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    if (isOnline && _uiState.value.holdings.isEmpty() && _uiState.value.error == ErrorType.NO_INTERNET) {
                        loadHoldings()
                    }
                }
        }
    }

    fun onAction(action: PortfolioAction) {
        when (action) {
            is PortfolioAction.Refresh -> onRefresh()
            is PortfolioAction.TabSelected -> onTabSelected(action.tab)
            is PortfolioAction.ToggleSummary -> toggleSummary()
            is PortfolioAction.ErrorConsumed -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun loadHoldings() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            getUserHoldingsUseCase().collect { resource ->
                _uiState.update { state ->
                    when (resource) {
                        is Resource.Loading -> {
                            if (state.holdings.isEmpty()) {
                                state.copy(isLoading = true, error = null)
                            } else {
                                state
                            }
                        }
                        is Resource.Success -> {
                            val holdings = resource.data
                            state.copy(
                                holdings = holdings,
                                summary = calculatePortfolioSummaryUseCase(holdings),
                                isLoading = false,
                                isRefreshing = false,
                                error = null
                            )
                        }
                        is Resource.Error -> {
                            state.copy(
                                error = resource.type,
                                isLoading = false,
                                isRefreshing = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onRefresh() {
        if (_uiState.value.selectedTab == PortfolioTab.POSITIONS) {
            viewModelScope.launch {
                _uiState.update { it.copy(isRefreshing = true) }
                delay(2000) // this is for fake loading
                _uiState.update { it.copy(isRefreshing = false) }
            }
        } else {
            _uiState.update { it.copy(isRefreshing = true) }
            loadHoldings()
        }
    }

    private fun onTabSelected(tab: PortfolioTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    private fun toggleSummary() {
        _uiState.update { it.copy(isExpanded = !it.isExpanded) }
    }
}
