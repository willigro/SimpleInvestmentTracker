package com.rittmann.common.datasource.di

import com.rittmann.common.datasource.dao.config.AppDatabase
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import dagger.Module
import dagger.Provides

@Module
class CryptoDaoModule {

    @Provides
    fun providesCryptoDao(appDatabase: AppDatabase): TradeDao = appDatabase.cryptoDao()
}