package com.digicolor.rahuldemo.presentation.ui.screens.portfolio

import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.domain.model.PortfolioSummary
import com.digicolor.rahuldemo.domain.usecase.CalculatePortfolioSummaryUseCase
import com.digicolor.rahuldemo.domain.usecase.GetUserHoldingsUseCase
import com.digicolor.rahuldemo.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioViewModelTest {

    private lateinit var viewModel: PortfolioViewModel
    private val getUserHoldingsUseCase: GetUserHoldingsUseCase = mockk()
    private val calculatePortfolioSummaryUseCase: CalculatePortfolioSummaryUseCase = mockk()
    
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct and loadHoldings is triggered`() = runTest {
        // Given
        val holdings = listOf(mockk<Holding>())
        val summary = mockk<PortfolioSummary>()
        coEvery { getUserHoldingsUseCase() } returns flowOf(Resource.Success(holdings))
        coEvery { calculatePortfolioSummaryUseCase(holdings) } returns summary

        // When
        viewModel = PortfolioViewModel(getUserHoldingsUseCase, calculatePortfolioSummaryUseCase)
        
        // Background collection to trigger StateFlow (Due to WhileSubscribed)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse("Loading should be false", state.isLoading)
        assertEquals(holdings, state.holdings)
        assertEquals(summary, state.summary)
        assertEquals(PortfolioTab.HOLDINGS, state.selectedTab)
        job.cancel()
    }

    @Test
    fun `loadHoldings updates state with error when use case fails`() = runTest {
        // Given
        val errorMessage = "Error fetching holdings"
        coEvery { getUserHoldingsUseCase() } returns flowOf(Resource.Error(errorMessage))

        // When
        viewModel = PortfolioViewModel(getUserHoldingsUseCase, calculatePortfolioSummaryUseCase)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        job.cancel()
    }

    @Test
    fun `onAction TabSelected updates selectedTab in state`() = runTest {
        // Given
        coEvery { getUserHoldingsUseCase() } returns flowOf(Resource.Loading)
        viewModel = PortfolioViewModel(getUserHoldingsUseCase, calculatePortfolioSummaryUseCase)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()

        // When
        viewModel.onAction(PortfolioAction.TabSelected(PortfolioTab.POSITIONS))
        advanceUntilIdle()

        // Then
        assertEquals(PortfolioTab.POSITIONS, viewModel.uiState.value.selectedTab)
        job.cancel()
    }

    @Test
    fun `onAction ToggleSummary updates isExpanded in state`() = runTest {
        // Given
        coEvery { getUserHoldingsUseCase() } returns flowOf(Resource.Success(emptyList()))
        coEvery { calculatePortfolioSummaryUseCase(any()) } returns mockk()
        viewModel = PortfolioViewModel(getUserHoldingsUseCase, calculatePortfolioSummaryUseCase)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()
        val initialExpanded = viewModel.uiState.value.isExpanded

        // When
        viewModel.onAction(PortfolioAction.ToggleSummary)
        advanceUntilIdle()

        // Then
        assertEquals(!initialExpanded, viewModel.uiState.value.isExpanded)
        job.cancel()
    }

    @Test
    fun `onAction Refresh triggers loadHoldings for HOLDINGS tab`() = runTest {
        // Given
        val holdings = listOf(mockk<Holding>())
        val summary = mockk<PortfolioSummary>()
        coEvery { getUserHoldingsUseCase() } returns flowOf(Resource.Success(holdings))
        coEvery { calculatePortfolioSummaryUseCase(holdings) } returns summary
        
        viewModel = PortfolioViewModel(getUserHoldingsUseCase, calculatePortfolioSummaryUseCase)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()

        // When
        viewModel.onAction(PortfolioAction.Refresh)
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isRefreshing)
        job.cancel()
    }
}

