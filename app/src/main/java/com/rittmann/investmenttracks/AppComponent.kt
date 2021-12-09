package com.rittmann.investmenttracks

import android.app.Application
import com.rittmann.crypto.di.CryptoModule
import com.rittmann.datasource.di.RoomModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, RoomModule::class, CryptoModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(application: YourApplication)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}