package com.digicolor.rahuldemo.di

import com.digicolor.rahuldemo.util.LiveNetworkMonitor
import com.digicolor.rahuldemo.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(
        liveNetworkMonitor: LiveNetworkMonitor
    ): NetworkMonitor
}