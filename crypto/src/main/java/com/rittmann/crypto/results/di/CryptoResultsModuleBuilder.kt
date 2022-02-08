package com.rittmann.crypto.results.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.di.CryptoDaoModule
import com.rittmann.common.viewmodel.ViewModelFactory
import com.rittmann.crypto.results.domain.CryptoResultsRepository
import com.rittmann.crypto.results.domain.CryptoResultsRepositoryImpl
import com.rittmann.crypto.results.ui.CryptoResultsActivity
import com.rittmann.crypto.results.ui.CryptoResultsNavigation
import com.rittmann.crypto.results.ui.CryptoResultsNavigationImpl
import com.rittmann.crypto.results.ui.CryptoResultsViewModel
import com.rittmann.di_utils.utils.ActivityScoped
import com.rittmann.di_utils.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CryptoResultsModuleBuilder {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CryptoResultsModuleDependencies::class])
    abstract fun bindCryptoResultsActivity(): CryptoResultsActivity
}

@Module(includes = [CryptoResultsRepositoryModule::class])
abstract class CryptoResultsModuleDependencies {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(CryptoResultsViewModel::class)
    abstract fun bindCryptoResultsViewModel(movementViewModel: CryptoResultsViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun providesCryptoResultsNavigation(activity: CryptoResultsActivity): CryptoResultsNavigation =
            CryptoResultsNavigationImpl(activity)
    }
}

@Module(includes = [CryptoDaoModule::class])
class CryptoResultsRepositoryModule {

    @Provides
    fun provideCryptoRepository(tradeDao: TradeDao): CryptoResultsRepository =
        CryptoResultsRepositoryImpl(tradeDao)
}