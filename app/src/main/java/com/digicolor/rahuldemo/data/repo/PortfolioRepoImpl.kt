package com.digicolor.rahuldemo.data.repo

import com.digicolor.rahuldemo.data.mapper.toDomainOrNull
import com.digicolor.rahuldemo.data.remote.PortfolioApi
import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.domain.repo.PortfolioRepo
import com.digicolor.rahuldemo.util.ErrorType
import com.digicolor.rahuldemo.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class PortfolioRepoImpl @Inject constructor(
    private val portfolioApi: PortfolioApi
) : PortfolioRepo {

    override fun getHoldings(): Flow<Resource<List<Holding>>> = flow {
        emit(Resource.Loading)
        try {
            val res = portfolioApi.getHoldings()

            val holdings = res.data
                ?.userHolding
                ?.mapNotNull { it.toDomainOrNull() }
                .orEmpty()

            emit(Resource.Success(holdings))
        } catch (e: Exception) {
            val errorType = when (e) {
                is UnknownHostException, is IOException -> ErrorType.NO_INTERNET
                else -> ErrorType.SERVER_ERROR
            }
            emit(Resource.Error(message = e.message ?: "An error occurred", type = errorType))
        }
    }.flowOn(Dispatchers.IO)
}