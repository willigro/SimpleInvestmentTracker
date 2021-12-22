package com.rittmann.crypto.keep.ui

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.rittmann.common.utils.DataBindingIdlingResource
import com.rittmann.common.utils.EspressoIdlingResource
import com.rittmann.common.utils.monitorActivity
import com.rittmann.common_test.EspressoUtil.checkToast
import com.rittmann.common_test.EspressoUtil.performClick
import com.rittmann.common_test.EspressoUtil.putValue
import com.rittmann.common_test.MainCoroutineRule
import com.rittmann.crypto.DaggerTestAppComponent
import com.rittmann.crypto.R
import com.rittmann.crypto.TestAppComponent
import com.rittmann.crypto.TestApplication
import com.rittmann.datasource.dao.config.AppDatabase
import com.rittmann.datasource.dao.interfaces.CryptoDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterCryptoMovementActivityTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // An idling resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    private val cryptoDao: CryptoDao? =
        AppDatabase.getDatabase(ApplicationProvider.getApplicationContext())?.cryptoDao()

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        val component: TestAppComponent = DaggerTestAppComponent.builder()
            .application(TestApplication())
            .dispatcherProvider(mainCoroutineRule.testDispatcherProvider)
            .build()

        component.inject(mainCoroutineRule.testDispatcherProvider)

        RegisterCryptoNavigationImpl.close = false

        // If i won't use it, I'll keep here just for check
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)

        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)

        cryptoDao?.deleteAll()
    }

    @Test
    fun fillTheFields_RegisterANewCryptoMovement_ShowTheSuccessDialog() =
        mainCoroutineRule.runBlockingTest {

            val activityScenario =
                ActivityScenario.launch(RegisterCryptoMovementActivity::class.java)
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