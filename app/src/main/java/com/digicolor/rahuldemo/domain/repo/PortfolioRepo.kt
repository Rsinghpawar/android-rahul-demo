package com.digicolor.rahuldemo.domain.repo

import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.util.Resource
import kotlinx.coroutines.flow.Flow

interface PortfolioRepo {
    fun getHoldings(): Flow<Resource<List<Holding>>>
}