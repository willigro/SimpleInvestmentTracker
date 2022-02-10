package com.rittmann.investmenttracks

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rittmann.baselifecycle.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.bottom_nav

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        MockCurrencies.mock(application)

        initNavigation()

//        KeepDepositActivity.start(this)
    }

    private fun initNavigation() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(bottom_nav, navController)
    }
}