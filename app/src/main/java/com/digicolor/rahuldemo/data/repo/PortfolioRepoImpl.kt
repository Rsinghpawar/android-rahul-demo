package com.digicolor.rahuldemo.data.repo

import com.digicolor.rahuldemo.data.mapper.toDomainOrNull
import com.digicolor.rahuldemo.data.remote.PortfolioApi
import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.domain.repo.PortfolioRepo
import com.digicolor.rahuldemo.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PortfolioRepoImpl(private val portfolioApi: PortfolioApi) : PortfolioRepo {

    override suspend fun getHoldings(): Flow<Resource<List<Holding>>> = flow {
        emit(Resource.Loading)
        try {
            val res = portfolioApi.getHoldings()

            val holdings = res.data
                ?.userHolding
                ?.mapNotNull { it.toDomainOrNull() } //todo : rahul can add logging here for null data
                .orEmpty()

            emit(Resource.Success(holdings))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Something went wrong"))
        }
    }.flowOn(Dispatchers.IO)
}