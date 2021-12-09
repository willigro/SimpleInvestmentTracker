package com.rittmann.datasource.di

import com.rittmann.datasource.dao.config.AppDatabase
import com.rittmann.datasource.dao.interfaces.CryptoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CryptoDaoModule {

    @Provides
    fun providesCryptoDao(appDatabase: AppDatabase): CryptoDao = appDatabase.cryptoDao()
}