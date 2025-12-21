package com.digicolor.rahuldemo.data.remote

import com.digicolor.rahuldemo.data.dto.PortfolioResponseDto
import retrofit2.http.GET

interface PortfolioApi {

    @GET("/")
    suspend fun getHoldings() : PortfolioResponseDto
}