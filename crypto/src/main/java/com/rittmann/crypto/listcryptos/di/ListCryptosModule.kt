package com.rittmann.crypto.listcryptos.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.datasource.dao.interfaces.CryptoDao
import com.rittmann.common.datasource.di.CryptoDaoModule
import com.rittmann.common.viewmodel.ViewModelFactory
import com.rittmann.crypto.listcryptos.domain.ListCryptosRepository
import com.rittmann.crypto.listcryptos.domain.ListCryptosRepositoryImpl
import com.rittmann.crypto.listcryptos.ui.ListCryptosFragment
import com.rittmann.crypto.listcryptos.ui.ListCryptosNavigation
import com.rittmann.crypto.listcryptos.ui.ListCryptosNavigationImpl
import com.rittmann.crypto.listcryptos.ui.ListCryptosViewModel
import com.rittmann.di_utils.utils.ActivityScoped
import com.rittmann.di_utils.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ListCryptosModuleBuilder {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ListCryptosModuleDependencies::class])
    abstract fun bindListCryptosFragment(): ListCryptosFragment
}

@Module(includes = [ListCryptosRepositoryModule::class])
abstract class ListCryptosModuleDependencies {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(ListCryptosViewModel::class)
    abstract fun bindListCryptosViewModel(movementViewModel: ListCryptosViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun providesListCryptosNavigation(fragment: ListCryptosFragment): ListCryptosNavigation =
            ListCryptosNavigationImpl(fragment)
    }
}

@Module(includes = [CryptoDaoModule::class])
class ListCryptosRepositoryModule {

    @Provides
    fun provideCryptoRepository(cryptoDao: CryptoDao): ListCryptosRepository =
        ListCryptosRepositoryImpl(cryptoDao)
}