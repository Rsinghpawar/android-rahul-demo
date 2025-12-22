package com.digicolor.rahuldemo.domain.usecase

import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.domain.repo.PortfolioRepo
import com.digicolor.rahuldemo.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetUserHoldingsUseCaseTest {

    private lateinit var getUserHoldingsUseCase: GetUserHoldingsUseCase
    private val portfolioRepo: PortfolioRepo = mockk()

    @Before
    fun setUp() {
        getUserHoldingsUseCase = GetUserHoldingsUseCase(portfolioRepo)
    }

    @Test
    fun `invoke should return Loading and Success states with Holdings list`() = runTest {
        // Given
        val holdings = listOf(
            Holding("SYMBOL1", 10, 100.0, 90.0, 95.0, 100.0),
            Holding("SYMBOL2", 5, 200.0, 180.0, 190.0, 100.0)
        )
        val flow = flowOf(
            Resource.Loading,
            Resource.Success(holdings)
        )
        coEvery { portfolioRepo.getHoldings() } returns flow
        // When
        val res = getUserHoldingsUseCase.invoke().toList()

        //Then
        assertEquals(2,res.size)
        assertTrue(res[0] is Resource.Loading)
        assertTrue(res[1] is Resource.Success)
        assertEquals(holdings, (res[1] as Resource.Success).data)
    }

    @Test
    fun `invoke should return Loading and Error states on failure`() = runTest {
        //Given
        val error  = "Something went wrong"
        val flow = flowOf(
            Resource.Loading,
            Resource.Error(error)
        )
        coEvery { portfolioRepo.getHoldings() } returns flow

        //when
        val res = getUserHoldingsUseCase.invoke().toList()

        //Then
        assertEquals(2,res.size)
        assertTrue(res[0] is Resource.Loading)
        assertTrue(res[1] is Resource.Error)
        assertEquals(error,(res[1] as Resource.Error).message)
    }
}
