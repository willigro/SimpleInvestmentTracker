package com.rittmann.investmenttracks

import android.app.Application
import com.rittmann.common.lifecycle.DispatcherProvider
import com.rittmann.crypto.di.CryptoModule
import com.rittmann.common.datasource.di.RoomModule
import com.rittmann.deposit.di.KeppDepositModuleBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, RoomModule::class, CryptoModule::class, KeppDepositModuleBuilder::class])
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(application: YourApplication)

    fun inject(dispatcherProvider: DispatcherProvider)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun dispatcherProvider(dispatcherProvider: DispatcherProvider): Builder

        fun build(): AppComponent
    }
}