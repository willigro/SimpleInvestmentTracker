package com.rittmann.datasource.di

import android.app.Application
import com.rittmann.datasource.dao.config.AppDatabase
import com.rittmann.datasource.dao.interfaces.CryptoDao
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