package com.rittmann.crypto

import android.app.Application
import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import com.rittmann.common.lifecycle.DispatcherProvider
import com.rittmann.crypto.di.CryptoModule
import com.rittmann.datasource.di.RoomModule
import com.rittmann.widgets.dialog.ModalUtil
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

class TestApplication : DaggerApplication() {

    private lateinit var appComponent: TestAppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerTestAppComponent.builder()
            .application(this)
            .dispatcherProvider(DefaultDispatcherProvider())
            .build()

        appComponent.inject(this)
        appComponent.inject(DefaultDispatcherProvider())

        ModalUtil.defaultTitle = getString(R.string.app_name)
        ModalUtil.resId = R.layout.dialog
        ModalUtil.resIdTitle = R.id.txt_title
        ModalUtil.resIdMessage = R.id.txt_msg
        ModalUtil.resIdBtnCancel = R.id.btn_cancel
        ModalUtil.resIdBtnConclude = R.id.btn_confirm

        return appComponent
    }
}

@Singleton
@Component(modules = [AndroidInjectionModule::class, RoomModule::class, CryptoModule::class])
interface TestAppComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: TestApplication)

    fun inject(dispatcherProvider: DispatcherProvider)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun dispatcherProvider(dispatcherProvider: DispatcherProvider): Builder

        fun build(): TestAppComponent
    }
}