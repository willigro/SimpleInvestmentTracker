package com.rittmann.common.datasource.di

import android.app.Application
import com.rittmann.common.datasource.dao.config.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(application: Application): AppDatabase =
        AppDatabase.getDatabase(application)!!

//    @Singleton
//    @Provides
//    fun providesCryptoDao(appDatabase: AppDatabase): CryptoDao = appDatabase.cryptoDao()
}