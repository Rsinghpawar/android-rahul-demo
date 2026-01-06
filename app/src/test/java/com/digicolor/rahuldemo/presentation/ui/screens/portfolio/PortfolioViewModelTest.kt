package com.digicolor.rahuldemo.presentation.ui.screens.portfolio

import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.domain.model.PortfolioSummary
import com.digicolor.rahuldemo.domain.usecase.CalculatePortfolioSummaryUseCase
import com.digicolor.rahuldemo.domain.usecase.GetUserHoldingsUseCase
import com.digicolor.rahuldemo.util.ErrorType
import com.digicolor.rahuldemo.util.NetworkMonitor
import com.digicolor.rahuldemo.util.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioViewModelTest {

    private lateinit var viewModel: PortfolioViewModel
    private val getUserHoldingsUseCase: GetUserHoldingsUseCase = mockk()
    private val calculatePortfolioSummaryUseCase: CalculatePortfolioSummaryUseCase = mockk()
    private val networkMonitor: NetworkMonitor = mockk()

    private val testDispatcher = StandardTestDispatcher()
    private val isOnlineFlow = MutableStateFlow(true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { networkMonitor.isOnline } returns isOnlineFlow
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
        viewModel = PortfolioViewModel(
            getUserHoldingsUseCase,
            calculatePortfolioSummaryUseCase,
            networkMonitor
        )
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(holdings, state.holdings)
        assertEquals(summary, state.summary)
    }

    @Test
    fun `loadHoldings updates state with error type when use case fails`() = runTest {
        // Given
        coEvery { getUserHoldingsUseCase() } returns flowOf(
            Resource.Error(
                "msg",
                ErrorType.NO_INTERNET
            )
        )

        // When
        viewModel = PortfolioViewModel(
            getUserHoldingsUseCase,
            calculatePortfolioSummaryUseCase,
            networkMonitor
        )
        advanceUntilIdle()

        // Then
        assertEquals(ErrorType.NO_INTERNET, viewModel.uiState.value.error)
    }

    @Test
    fun `onAction ErrorConsumed clears error in state`() = runTest {
        // Given
        coEvery { getUserHoldingsUseCase() } returns flowOf(
            Resource.Error(
                "msg",
                ErrorType.SERVER_ERROR
            )
        )
        viewModel = PortfolioViewModel(
            getUserHoldingsUseCase,
            calculatePortfolioSummaryUseCase,
            networkMonitor
        )
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()
        assertEquals(ErrorType.SERVER_ERROR, viewModel.uiState.value.error)

        // When
        viewModel.onAction(PortfolioAction.ErrorConsumed)
        advanceUntilIdle()

        // Then
        assertNull(viewModel.uiState.value.error)
        job.cancel()
    }

    @Test
    fun `automatic reload when internet comes back and state is empty error`() = runTest {
        // Given
        isOnlineFlow.value = false
        coEvery { getUserHoldingsUseCase() } returns flowOf(
            Resource.Error(
                "msg",
                ErrorType.NO_INTERNET
            )
        )

        viewModel = PortfolioViewModel(
            getUserHoldingsUseCase,
            calculatePortfolioSummaryUseCase,
            networkMonitor
        )

        advanceUntilIdle()
        assertEquals(ErrorType.NO_INTERNET, viewModel.uiState.value.error)

        // Setup for success on second call
        val holdings = listOf(mockk<Holding>())
        coEvery { getUserHoldingsUseCase() } returns flowOf(Resource.Success(holdings))
        coEvery { calculatePortfolioSummaryUseCase(holdings) } returns mockk()

        // When
        isOnlineFlow.value = true
        advanceUntilIdle()

        // Then
        assertEquals(holdings, viewModel.uiState.value.holdings)
        assertNull(viewModel.uiState.value.error)
    }
}
