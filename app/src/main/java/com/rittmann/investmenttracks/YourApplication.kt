package com.rittmann.investmenttracks

import com.rittmann.common.lifecycle.DefaultDispatcherProvider
import com.rittmann.widgets.dialog.ModalUtil
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class YourApplication : DaggerApplication() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        ModalUtil.defaultTitle = getString(R.string.app_name)
        ModalUtil.resId = R.layout.dialog
        ModalUtil.resIdTitle = R.id.txt_title
        ModalUtil.resIdMessage = R.id.txt_msg
        ModalUtil.resIdBtnCancel = R.id.btn_cancel
        ModalUtil.resIdBtnConclude = R.id.btn_confirm
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .dispatcherProvider(DefaultDispatcherProvider())
            .build()

        appComponent.inject(this)
        appComponent.inject(DefaultDispatcherProvider())

        return appComponent
    }
}