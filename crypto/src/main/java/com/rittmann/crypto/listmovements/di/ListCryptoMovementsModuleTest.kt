package com.rittmann.crypto.listmovements.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rittmann.common.viewmodel.ViewModelFactory
import com.rittmann.crypto.listmovements.domain.ListCryptoMovementsRepository
import com.rittmann.crypto.listmovements.ui.ListCryptoMovementsFragment
import com.rittmann.crypto.listmovements.ui.ListCryptoMovementsNavigation
import com.rittmann.crypto.listmovements.ui.ListCryptoMovementsNavigationImpl
import com.rittmann.crypto.listmovements.ui.ListCryptoMovementsViewModel
import com.rittmann.common.datasource.dao.interfaces.TradeDao
import com.rittmann.common.datasource.di.CryptoDaoModule
import com.rittmann.crypto.listmovements.domain.ListCryptoMovementsRepositoryImplTest
import com.rittmann.di_utils.utils.ActivityScoped
import com.rittmann.di_utils.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ListCryptoMovementsModuleBuilderTest {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ListCryptoMovementsModuleDependenciesTest::class])
    abstract fun bindListCryptoMovementsActivity(): ListCryptoMovementsFragment
}

@Module(includes = [ListCryptoMovementsRepositoryModuleTest::class])
abstract class ListCryptoMovementsModuleDependenciesTest {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(ListCryptoMovementsViewModel::class)
    abstract fun bindListCryptoMovementsViewModel(movementViewModel: ListCryptoMovementsViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun providesListCryptoMovementsNavigation(fragment: ListCryptoMovementsFragment): ListCryptoMovementsNavigation =
            ListCryptoMovementsNavigationImpl(fragment)
    }
}

@Module(includes = [CryptoDaoModule::class])
class ListCryptoMovementsRepositoryModuleTest {

    @Provides
    fun provideCryptoRepository(tradeDao: TradeDao): ListCryptoMovementsRepository =
        ListCryptoMovementsRepositoryImplTest(tradeDao)
}