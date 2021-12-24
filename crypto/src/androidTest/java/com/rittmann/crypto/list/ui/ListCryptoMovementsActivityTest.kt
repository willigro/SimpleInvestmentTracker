package com.rittmann.crypto.list.ui

import com.rittmann.crypto.R
import androidx.test.core.app.ApplicationProvider
import com.rittmann.common.utils.monitorActivity
import com.rittmann.common_test.EspressoUtil.checkValue
import com.rittmann.common_test.EspressoUtil.getCurrentActivity
import com.rittmann.common_test.EspressoUtil.performClick
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.common_test.mock.newCryptoMovementMock
import com.rittmann.crypto.BaseTestActivity
import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity
import com.rittmann.datasource.dao.config.AppDatabase
import com.rittmann.datasource.dao.interfaces.CryptoDao
import com.rittmann.datasource.result.ResultEvent
import com.rittmann.datasource.result.succeeded
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListCryptoMovementsActivityTest : BaseTestActivity() {

    private val cryptoDao: CryptoDao? =
        AppDatabase.getDatabase(ApplicationProvider.getApplicationContext())?.cryptoDao()

    @Before
    fun finish() {
        cryptoDao?.deleteAll()
    }

    @Test
    fun listTheCryptoMovements() = runBlockingTest {

        val listInserted = arrayListOf(newCryptoMovementMock)

        cryptoDao?.insert(listInserted)

        val activityScenario = getActivity<ListCryptoMovementsActivity>()

        dataBindingIdlingResource.monitorActivity(activityScenario)

        activityScenario.onActivity {
            val result = it.viewModel.cryptoMovementsList.getOrAwaitValue()

            assertThat(result.succeeded, `is`(true))

            (result as ResultEvent.Success).data.apply {
                assertThat(size, `is`(listInserted.size))
            }
        }

        checkValue(newCryptoMovementMock.name)
        checkValue(newCryptoMovementMock.date)
        checkValue(newCryptoMovementMock.type.value)
        checkValue(newCryptoMovementMock.boughtAmount.toString())
        checkValue(newCryptoMovementMock.currentValue.toString())
        checkValue(newCryptoMovementMock.totalValue.toString())
        checkValue(newCryptoMovementMock.tax.toString())

        activityScenario.close()
    }

    @Test
    fun openTheRegisterCrypto_WhenClickOnButton() {
        val activityScenario = getActivity<ListCryptoMovementsActivity>()

        dataBindingIdlingResource.monitorActivity(activityScenario)

        performClick(R.id.button_register_new_crypto)

        assertThat(getCurrentActivity() is RegisterCryptoMovementActivity, `is`(true))

        activityScenario.close()
    }
}