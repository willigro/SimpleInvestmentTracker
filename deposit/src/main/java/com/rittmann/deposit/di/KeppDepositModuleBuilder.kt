package com.rittmann.deposit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.di.CryptoDaoModule
import com.rittmann.common.viewmodel.ViewModelFactory
import com.rittmann.deposit.domain.KeepDepositRepository
import com.rittmann.deposit.domain.KeepDepositRepositoryImpl
import com.rittmann.deposit.keep.KeepDepositActivity
import com.rittmann.deposit.keep.KeepDepositMovementViewModel
import com.rittmann.di_utils.utils.ActivityScoped
import com.rittmann.di_utils.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class KeppDepositModuleBuilder {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [KeepDepositModuleDependencies::class])
    abstract fun bindKeepDepositActivity(): KeepDepositActivity
}

@Module(includes = [KeepDepositRepositoryModule::class])
abstract class KeepDepositModuleDependencies {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(KeepDepositMovementViewModel::class)
    abstract fun bindKeepDepositViewModel(movementViewModel: KeepDepositMovementViewModel): ViewModel

//    @Module
//    companion object {
//
//        @Provides
//        @JvmStatic
//        fun providesRegisterCryptoNavigation(activity: KeepDepositActivity): RegisterCryptoNavigation =
//            RegisterCryptoNavigationImpl(activity)
//    }
}


@Module(includes = [CryptoDaoModule::class])
class KeepDepositRepositoryModule {

    @Provides
    fun provideCryptoRepository(tradeDao: TradeDao): KeepDepositRepository =
        KeepDepositRepositoryImpl(tradeDao)
}