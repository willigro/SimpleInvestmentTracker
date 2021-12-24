package com.rittmann.crypto.keep.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.rittmann.common.utils.monitorActivity
import com.rittmann.common_test.EspressoUtil.checkToast
import com.rittmann.common_test.EspressoUtil.performClick
import com.rittmann.common_test.EspressoUtil.putValue
import com.rittmann.crypto.BaseTestActivity
import com.rittmann.crypto.R
import com.rittmann.datasource.dao.config.AppDatabase
import com.rittmann.datasource.dao.interfaces.CryptoDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
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

            checkToast(
                ApplicationProvider.getApplicationContext<Context>()
                    .getString(R.string.new_crypto_movement_was_registered)
            )

            val registeredCrypto = cryptoDao?.selectAll()?.last()

            assertThat(registeredCrypto?.name, `is`(name))
            assertThat(registeredCrypto?.date, `is`(date))
            assertThat(registeredCrypto?.boughtAmount, `is`(boughtAmount.toInt()))
            assertThat(registeredCrypto?.currentValue, `is`(currentValue.toDouble()))
            assertThat(registeredCrypto?.totalValue, `is`(totalValue.toDouble()))
            assertThat(registeredCrypto?.tax, `is`(tax.toDouble()))

            activityScenario.close()
        }
}