package com.rittmann.common_test

import android.content.Context
import androidx.test.core.app.ApplicationProvider

fun getStringTest(resId: Int) =
    ApplicationProvider.getApplicationContext<Context>().getString(resId)