package com.rittmann.crypto.keep.ui

import android.view.View
import com.rittmann.common.utils.DateUtil
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.crypto.BaseActivityTest
import java.util.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class RegisterCryptoMovementActivityTest : BaseActivityTest<RegisterCryptoMovementActivity>() {

    @Before
    fun setupActivity() {
        configureActivity<RegisterCryptoMovementActivity>()
    }

    @Test
    fun t() {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -1)
        }

        forceResume()

        activity.viewModel.changeDate(yesterday)
        activity.viewModel.retrieveDate()

        activity.viewModel.dateRetrieved.getOrAwaitValue()
        assertThat(activity.binding.txtCryptoDate.text, `is`(DateUtil.simpleDateFormat(yesterday)))

        assertThat(activity.binding.btnRegister.visibility, `is`(View.VISIBLE))
    }
}