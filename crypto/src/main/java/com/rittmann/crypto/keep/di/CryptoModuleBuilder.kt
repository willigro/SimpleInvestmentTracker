package com.rittmann.crypto.keep.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.viewmodel.ViewModelFactory
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepository
import com.rittmann.crypto.keep.domain.RegisterCryptoMovementRepositoryImpl
import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity
import com.rittmann.crypto.keep.ui.RegisterCryptoMovementViewModel
import com.rittmann.crypto.keep.ui.RegisterCryptoNavigation
import com.rittmann.crypto.keep.ui.RegisterCryptoNavigationImpl
import com.rittmann.datasource.dao.interfaces.CryptoDao
import com.rittmann.datasource.di.CryptoDaoModule
import com.rittmann.di_utils.utils.ActivityScoped
import com.rittmann.di_utils.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CryptoModuleBuilder {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CryptoModuleDependencies::class])
    abstract fun bindRegisterCryptoActivity(): RegisterCryptoMovementActivity
}

@Module(includes = [CryptoRepositoryModule::class])
abstract class CryptoModuleDependencies {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(RegisterCryptoMovementViewModel::class)
    abstract fun bindRegisterCryptoViewModel(movementViewModel: RegisterCryptoMovementViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun providesRegisterCryptoNavigation(activity: RegisterCryptoMovementActivity): RegisterCryptoNavigation =
            RegisterCryptoNavigationImpl(activity)
    }
}

@Module(includes = [CryptoDaoModule::class])
class CryptoRepositoryModule {

    @Provides
    fun provideCryptoRepository(cryptoDao: CryptoDao): RegisterCryptoMovementRepository =
        RegisterCryptoMovementRepositoryImpl(cryptoDao)
}