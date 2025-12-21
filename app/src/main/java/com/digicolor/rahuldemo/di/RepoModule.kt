package com.digicolor.rahuldemo.di

import com.digicolor.rahuldemo.data.repo.PortfolioRepoImpl
import com.digicolor.rahuldemo.domain.repo.PortfolioRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindPortfolioRepo(
        portfolioRepoImpl: PortfolioRepoImpl
    ): PortfolioRepo
}