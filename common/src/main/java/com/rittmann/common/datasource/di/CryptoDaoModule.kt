package com.rittmann.common.datasource.di

import com.rittmann.common.datasource.dao.config.AppDatabase
import com.rittmann.common.datasource.dao.interfaces.CryptoDao
import dagger.Module
import dagger.Provides

@Module
class CryptoDaoModule {

    @Provides
    fun providesCryptoDao(appDatabase: AppDatabase): CryptoDao = appDatabase.cryptoDao()
}