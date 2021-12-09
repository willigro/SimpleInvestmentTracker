package com.rittmann.crypto.keep.ui

import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rittmann.common.utils.DataBindingIdlingResource
import com.rittmann.common.utils.EspressoIdlingResource
import com.rittmann.common.utils.monitorActivity
import com.rittmann.common_test.EspressoUtil.checkValue
import com.rittmann.common_test.EspressoUtil.performClick
import com.rittmann.common_test.EspressoUtil.putValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.rittmann.crypto.R

@RunWith(AndroidJUnit4::class)
class RegisterCryptoMovementActivityTest {
    // An idling resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        // If i won't use it, I'll keep here just for check
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        IdlingRegistry.getInstance().register(dataBindingIdlingResource)


        val app = InstrumentationRegistry.getTargetContext().applicationContext as YourApp
        testAppComponent = DaggerTestAppComponent.builder()
            .appModule(AppModule(app))
            .apiModule(/** mock Api Module**/)
            .prefModule(/** mock preference Module **/)
            .build()
        app.appComponent = testAppComponent
        testAppComponent.inject(this)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)

        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun registerCryptoMovement() = runBlocking {

        val activityScenario = ActivityScenario.launch(RegisterCryptoMovementActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario) // LOOK HERE

        putValue(R.id.edit_crypto_name, "Name")
        putValue(R.id.edit_crypto_date, "00/11/2222")
        putValue(R.id.edit_crypto_bought_amount, "10")
        putValue(R.id.edit_crypto_current_value, "5")
        putValue(R.id.edit_crypto_total_value, "50")
        putValue(R.id.edit_crypto_tax, "1")

        performClick(R.id.btn_register)

        checkValue(ApplicationProvider.getApplicationContext<Context>().getString(R.string.new_crypto_movement_was_registered))

        activityScenario.close()
    }
}