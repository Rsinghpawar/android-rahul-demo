package com.digicolor.rahuldemo.presentation.ui.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digicolor.rahuldemo.domain.usecase.CalculatePortfolioSummaryUseCase
import com.digicolor.rahuldemo.domain.usecase.GetUserHoldingsUseCase
import com.digicolor.rahuldemo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getUserHoldingsUseCase: GetUserHoldingsUseCase,
    private val calculatePortfolioSummaryUseCase: CalculatePortfolioSummaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PortfolioUiState())
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()
        .onStart { loadHoldings() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PortfolioUiState(isLoading = true)
        )

    private fun loadHoldings() {
        viewModelScope.launch {
            getUserHoldingsUseCase().collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        val holdings = resource.data ?: emptyList()
                        _uiState.value = _uiState.value.copy(
                            holdings = holdings,
                            summary = calculatePortfolioSummaryUseCase(holdings),
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = resource.message,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            if (_uiState.value.selectedTab == PortfolioTab.POSITIONS) {
                delay(2000)
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            } else {
                loadHoldings()
            }
        }
    }

    fun onTabSelected(tab: PortfolioTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun toggleSummary() {
        _uiState.value = _uiState.value.copy(isExpanded = !_uiState.value.isExpanded)
    }
}