package com.rittmann.crypto.keep.ui

import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import com.rittmann.common.datasource.dao.config.AppDatabase
import com.rittmann.common.datasource.dao.interfaces.CryptoDao
import com.rittmann.common.utils.monitorActivity
import com.rittmann.common_test.EspressoUtil.checkValue
import com.rittmann.common_test.EspressoUtil.performClick
import com.rittmann.common_test.EspressoUtil.putValue
import com.rittmann.common_test.mock.newCryptoMovementMock
import com.rittmann.crypto.BaseTestActivity
import com.rittmann.crypto.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@LargeTest
class RegisterCryptoMovementActivityTest : BaseTestActivity() {

    private val cryptoDao: CryptoDao? =
        AppDatabase.getDatabase(ApplicationProvider.getApplicationContext())?.cryptoDao()

    @Before
    fun setup() {
        RegisterCryptoNavigationImpl.close = false
    }

    @After
    fun finish() {
        cryptoDao?.deleteAll()
    }

    @Test
    fun fillTheFields_RegisterANewCryptoMovement_ShowTheSuccessDialog() = runBlockingTest {

        val activityScenario = getActivity<RegisterCryptoMovementActivity>()

        dataBindingIdlingResource.monitorActivity(activityScenario) // LOOK HERE

        val name = "Name"
        val date = "00/11/2222"
        val boughtAmount = "10"
        val currentValue = "5"
        val totalValue = "50"
        val tax = "1"

        putValue(R.id.edit_crypto_name, R.id.edit_custom_id, name)
        putValue(R.id.edit_crypto_date, R.id.edit_custom_id, date)
        putValue(R.id.edit_crypto_bought_amount, R.id.edit_custom_id, boughtAmount)
        putValue(R.id.edit_crypto_current_value, R.id.edit_custom_id, currentValue)
        putValue(R.id.edit_crypto_total_value, R.id.edit_custom_id, totalValue)
        putValue(R.id.edit_crypto_tax, R.id.edit_custom_id, tax)

        performClick(R.id.btn_register)

        val registeredCrypto = cryptoDao?.selectAll()?.last()

        assertThat(registeredCrypto?.name, `is`(name))
        assertThat(registeredCrypto?.date, `is`(date))
        assertThat(registeredCrypto?.operatedAmount, `is`(boughtAmount.toDouble()))
        assertThat(registeredCrypto?.currentValue, `is`(currentValue.toDouble()))
        assertThat(registeredCrypto?.totalValue, `is`(totalValue.toDouble()))
        assertThat(registeredCrypto?.tax, `is`(tax.toDouble()))

        activityScenario.close()
    }


    @Test
    fun fillTheFields_RegisterANewCryptoMovement_ThenUpdateThisCrypto_ShowTheSuccessDialog() =
        runBlockingTest {

            val activityScenario = getActivity<RegisterCryptoMovementActivity>()

            dataBindingIdlingResource.monitorActivity(activityScenario) // LOOK HERE

            val name = "Name"
            val date = "00/11/2222"
            val boughtAmount = "10"
            val currentValue = "5"
            val totalValue = "50"
            val tax = "1"

            putValue(R.id.edit_crypto_name, R.id.edit_custom_id, name)
            putValue(R.id.edit_crypto_date, R.id.edit_custom_id, date)
            putValue(R.id.edit_crypto_bought_amount, R.id.edit_custom_id, boughtAmount)
            putValue(R.id.edit_crypto_current_value, R.id.edit_custom_id, currentValue)
            putValue(R.id.edit_crypto_total_value, R.id.edit_custom_id, totalValue)
            putValue(R.id.edit_crypto_tax, R.id.edit_custom_id, tax)

            performClick(R.id.btn_register)

            val registeredCrypto = cryptoDao?.selectAll()?.last()

            assertThat(registeredCrypto?.name, `is`(name))
            assertThat(registeredCrypto?.date, `is`(date))
            assertThat(registeredCrypto?.operatedAmount, `is`(boughtAmount.toDouble()))
            assertThat(registeredCrypto?.currentValue, `is`(currentValue.toDouble()))
            assertThat(registeredCrypto?.totalValue, `is`(totalValue.toDouble()))
            assertThat(registeredCrypto?.tax, `is`(tax.toDouble()))

            // Updating

            val nameUpdate = "Name Two"
            val dateUpdate = "14/11/1990"
            val boughtAmountUpdate = "25.0"
            val currentValueUpdate = "10.0"
            val totalValueUpdate = "250.0"
            val taxUpdate = "2.0"

            putValue(R.id.edit_crypto_name, R.id.edit_custom_id, nameUpdate)
            putValue(R.id.edit_crypto_date, R.id.edit_custom_id, dateUpdate)
            putValue(R.id.edit_crypto_bought_amount, R.id.edit_custom_id, boughtAmountUpdate)
            putValue(R.id.edit_crypto_current_value, R.id.edit_custom_id, currentValueUpdate)
            putValue(R.id.edit_crypto_total_value, R.id.edit_custom_id, totalValueUpdate)
            putValue(R.id.edit_crypto_tax, R.id.edit_custom_id, taxUpdate)

            performClick(R.id.btn_register)

            val updatedCrypto = cryptoDao?.selectAll()?.last()

            assertThat(updatedCrypto?.name, `is`(nameUpdate))
            assertThat(updatedCrypto?.date, `is`(dateUpdate))
            assertThat(updatedCrypto?.operatedAmount, `is`(boughtAmountUpdate.toDouble()))
            assertThat(updatedCrypto?.currentValue, `is`(currentValueUpdate.toDouble()))
            assertThat(updatedCrypto?.totalValue, `is`(totalValueUpdate.toDouble()))
            assertThat(updatedCrypto?.tax, `is`(taxUpdate.toDouble()))

            assertThat(registeredCrypto?.id, `is`(updatedCrypto?.id))

            activityScenario.close()
        }

    @Test
    fun showAnExistentCryptoMovent_UpdateIt_ShowTheSuccessDialog() = runBlockingTest {

        val resultId = cryptoDao?.insert(newCryptoMovementMock) ?: 0L

        val crypto = newCryptoMovementMock.copy(id = resultId)

        val activityScenario = getActivity<RegisterCryptoMovementActivity>()

        dataBindingIdlingResource.monitorActivity(activityScenario) // LOOK HERE

        activityScenario.onActivity {
            it.viewModel.attachCryptoMovementForUpdate(
                crypto
            )
        }

        checkValue(R.id.edit_crypto_name, R.id.edit_custom_id, crypto.name)
        checkValue(R.id.edit_crypto_date, R.id.edit_custom_id, crypto.date)
        checkValue(
            R.id.edit_crypto_bought_amount,
            R.id.edit_custom_id,
            crypto.operatedAmount.toString()
        )
        checkValue(
            R.id.edit_crypto_current_value,
            R.id.edit_custom_id,
            crypto.currentValue.toString()
        )
        checkValue(R.id.edit_crypto_total_value, R.id.edit_custom_id, crypto.totalValue.toString())
        checkValue(R.id.edit_crypto_tax, R.id.edit_custom_id, crypto.tax.toString())

        val name = "Name"
        val date = "00/11/2222"
        val operatedAmount = "10.0"
        val currentValue = "5.0"
        val totalValue = "50.0"
        val tax = "1.0"

        putValue(R.id.edit_crypto_name, R.id.edit_custom_id, name)
        putValue(R.id.edit_crypto_date, R.id.edit_custom_id, date)
        putValue(R.id.edit_crypto_bought_amount, R.id.edit_custom_id, operatedAmount)
        putValue(R.id.edit_crypto_current_value, R.id.edit_custom_id, currentValue)
        putValue(R.id.edit_crypto_total_value, R.id.edit_custom_id, totalValue)
        putValue(R.id.edit_crypto_tax, R.id.edit_custom_id, tax)

        performClick(R.id.btn_register)

        val registeredCrypto = cryptoDao?.selectAll()?.last()

        assertThat(registeredCrypto?.name, `is`(name))
        assertThat(registeredCrypto?.date, `is`(date))
        assertThat(registeredCrypto?.operatedAmount, `is`(operatedAmount.toDouble()))
        assertThat(registeredCrypto?.currentValue, `is`(currentValue.toDouble()))
        assertThat(registeredCrypto?.totalValue, `is`(totalValue.toDouble()))
        assertThat(registeredCrypto?.tax, `is`(tax.toDouble()))

        activityScenario.close()
    }
}