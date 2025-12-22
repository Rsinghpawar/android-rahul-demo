package com.digicolor.rahuldemo.data.repo

import com.digicolor.rahuldemo.data.dto.PortfolioResponseDto
import com.digicolor.rahuldemo.data.remote.PortfolioApi
import com.digicolor.rahuldemo.util.ErrorType
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
import java.io.IOException
import java.net.UnknownHostException

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
        assertEquals("MAHABANK", holdings[0].symbol)
    }

    @Test
    fun `getHoldings - UnknownHostException - returns NO_INTERNET error type`() = runTest {
        // Given
        coEvery { portfolioApi.getHoldings() } throws UnknownHostException("No connection")

        // When
        val result = portfolioRepo.getHoldings().toList()

        // Then
        assertTrue(result[0] is Resource.Loading)
        val errorState = result[1] as Resource.Error
        assertEquals(ErrorType.NO_INTERNET, errorState.type)
    }

    @Test
    fun `getHoldings - IOException - returns NO_INTERNET error type`() = runTest {
        // Given
        coEvery { portfolioApi.getHoldings() } throws IOException("Timeout")

        // When
        val result = portfolioRepo.getHoldings().toList()

        // Then
        val errorState = result[1] as Resource.Error
        assertEquals(ErrorType.NO_INTERNET, errorState.type)
    }

    @Test
    fun `getHoldings - Other Exception - returns SERVER_ERROR error type`() = runTest {
        // Given
        coEvery { portfolioApi.getHoldings() } throws Exception("Server crash")

        // When
        val result = portfolioRepo.getHoldings().toList()

        // Then
        val errorState = result[1] as Resource.Error
        assertEquals(ErrorType.SERVER_ERROR, errorState.type)
    }

    private fun getJsonContent(fileName: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        return inputStream?.bufferedReader()?.use { it.readText() } ?: ""
    }
}
