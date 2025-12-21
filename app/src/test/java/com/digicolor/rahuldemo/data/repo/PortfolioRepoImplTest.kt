package com.digicolor.rahuldemo.data.repo

import com.digicolor.rahuldemo.data.dto.PortfolioResponseDto
import com.digicolor.rahuldemo.data.remote.PortfolioApi
import com.digicolor.rahuldemo.util.Resource
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PortfolioRepoImplTest {

    private lateinit var portfolioRepo: PortfolioRepoImpl
    private val portfolioApi: PortfolioApi = mockk()
    private val gson = Gson()

    @Before
    fun setUp() {
        portfolioRepo = PortfolioRepoImpl(portfolioApi)
    }

    @Test
    fun `getHoldings - Success - returns loading and success states with mapped data`() = runTest {
        // Given
        val jsonContent = getJsonContent("portfolio_response.json")
        val mockResponse = gson.fromJson(jsonContent, PortfolioResponseDto::class.java)
        coEvery { portfolioApi.getHoldings() } returns mockResponse

        // When
        val result = portfolioRepo.getHoldings().toList()

        // Then
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        
        val holdings = (result[1] as Resource.Success).data
        assertEquals(2, holdings.size)
        assertEquals("MAHABANK", holdings.get(0).symbol)
        assertEquals(990, holdings[0].quantity)
        assertEquals(38.05, holdings.get(0).lastTradedPrice, 0.0)
    }

    @Test
    fun `getHoldings - Failure - returns loading and error states`() = runTest {
        // Given
        val errorMessage = "Network Error"
        coEvery { portfolioApi.getHoldings() } throws Exception(errorMessage)

        // When
        val result = portfolioRepo.getHoldings().toList()

        // Then
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
        assertEquals(errorMessage, (result[1] as Resource.Error).message)
    }

    private fun getJsonContent(fileName: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        return inputStream?.bufferedReader()?.use { it.readText() } ?: ""
    }
}
