package com.rittmann.crypto.list.ui

import androidx.test.core.app.ApplicationProvider
import com.rittmann.common.datasource.dao.config.AppDatabase
import com.rittmann.common.datasource.dao.interfaces.CryptoDao
import com.rittmann.common.datasource.result.ResultEvent
import com.rittmann.common.datasource.result.succeeded
import com.rittmann.common.extensions.orZero
import com.rittmann.common.utils.monitorActivity
import com.rittmann.common_test.EspressoUtil.checkValue
import com.rittmann.common_test.EspressoUtil.getCurrentActivity
import com.rittmann.common_test.EspressoUtil.performBack
import com.rittmann.common_test.EspressoUtil.performClick
import com.rittmann.common_test.getOrAwaitValue
import com.rittmann.common_test.getStringTest
import com.rittmann.common_test.mock.newCryptoMovementMock
import com.rittmann.crypto.BaseTestActivity
import com.rittmann.crypto.R
import com.rittmann.crypto.keep.ui.RegisterCryptoMovementActivity
import com.rittmann.crypto.list.domain.ListCryptoMovementsRepositoryImplTest
import com.rittmann.widgets.dialog.ModalUtil
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListCryptoMovementsActivityTest : BaseTestActivity() {

    private val cryptoDao: CryptoDao? =
        AppDatabase.getDatabase(ApplicationProvider.getApplicationContext())?.cryptoDao()

    @Before
    fun finish() {
        MockKAnnotations.init(this)
        cryptoDao?.deleteAll()
    }

    @Test
    fun listTheCryptoMovements() = runBlockingTest {

        ListCryptoMovementsRepositoryImplTest.returnsError = false

        val listInserted = arrayListOf(newCryptoMovementMock)

        cryptoDao?.insert(listInserted)

        val activityScenario = getActivity<ListCryptoMovementsActivity>()

        dataBindingIdlingResource.monitorActivity(activityScenario)

        var totalEarned = ""
        var totalInvested = ""

        activityScenario.onActivity {
            val result = it.viewModel.cryptoMovementsList.getOrAwaitValue()

            totalEarned = it.viewModel.totalValueEarned.getOrAwaitValue()
            totalInvested = it.viewModel.totalValueInvested.getOrAwaitValue()

            assertThat(result.succeeded, `is`(true))

            (result as ResultEvent.Success).data.apply {
                assertThat(size, `is`(listInserted.size))
            }
        }

        checkValue(R.id.adapter_crypto_movement_name, newCryptoMovementMock.name)
        checkValue(R.id.adapter_crypto_movement_date, newCryptoMovementMock.date)
        checkValue(R.id.adapter_crypto_movement_type, newCryptoMovementMock.type.value)
        checkValue(
            R.id.adapter_crypto_movement_bought_amount,
            newCryptoMovementMock.boughtAmount.toString()
        )
        checkValue(
            R.id.adapter_crypto_movement_current_value,
            newCryptoMovementMock.currentValue.toString()
        )
        checkValue(
            R.id.adapter_crypto_movement_total_value,
            newCryptoMovementMock.totalValue.toString()
        )
        checkValue(R.id.adapter_crypto_movement_tax, newCryptoMovementMock.tax.toString())

        checkValue(R.id.txt_total_earned, totalEarned)
        checkValue(R.id.txt_total_invested, totalInvested)

        activityScenario.close()
    }

    @Test
    fun listTheCryptoMovements_LaunchAnError_ShowErrorMessage() = runBlockingTest {

        ListCryptoMovementsRepositoryImplTest.returnsError = true

        val listInserted = arrayListOf(newCryptoMovementMock)

        cryptoDao?.insert(listInserted)

        val activityScenario = getActivity<ListCryptoMovementsActivity>()

        dataBindingIdlingResource.monitorActivity(activityScenario)

        activityScenario.onActivity {
            val result = it.viewModel.cryptoMovementsList.getOrAwaitValue()

            assertThat(result.succeeded, `is`(false))

            (result as ResultEvent.Error).exception.apply {
                assertThat(this, Matchers.instanceOf(Exception::class.java))
            }
        }

        checkValue(getStringTest(R.string.list_crypto_error))

        performBack(ModalUtil.resIdBtnConclude.orZero())

        checkValue(R.id.txt_total_earned, "0.0")
        checkValue(R.id.txt_total_invested, "0.0")

        activityScenario.close()
    }

    @Test
    fun openTheRegisterCrypto_WhenClickOnButton() {
        ListCryptoMovementsRepositoryImplTest.returnsError = false

        val activityScenario = getActivity<ListCryptoMovementsActivity>()

        dataBindingIdlingResource.monitorActivity(activityScenario)

        performClick(R.id.button_register_new_crypto)

        assertThat(getCurrentActivity() is RegisterCryptoMovementActivity, `is`(true))

        activityScenario.close()
    }
}