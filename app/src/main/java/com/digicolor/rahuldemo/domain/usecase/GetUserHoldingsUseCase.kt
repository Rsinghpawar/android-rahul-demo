package com.digicolor.rahuldemo.domain.usecase

import com.digicolor.rahuldemo.domain.model.Holding
import com.digicolor.rahuldemo.domain.repo.PortfolioRepo
import com.digicolor.rahuldemo.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserHoldingsUseCase @Inject constructor(
    private val portfolioRepo: PortfolioRepo
) {
    operator fun invoke(): Flow<Resource<List<Holding>>> = portfolioRepo.getHoldings()
}