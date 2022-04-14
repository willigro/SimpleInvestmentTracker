package com.rittmann.common_test

import android.content.Context
import androidx.test.core.app.ApplicationProvider

fun getStringTest(resId: Int) =
    ApplicationProvider.getApplicationContext<Context>().getString(resId)

const val TEST_DELAY = 100L